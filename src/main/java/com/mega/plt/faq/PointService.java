package com.mega.plt.faq;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

	@Autowired
	PointDAO dao;

	static int[] sortReady;// 내가 정렬해야 할 원소의 배열

	public int[] mergeSort() {
		mergeSort m = new mergeSort();
		sortReady = new int[dao.list().size()];// DB의 데이터 양 만큼 배열의 크기 지정
		for (int i = 0; i < dao.list().size(); i++) {
			// DB에서 정렬할 값 하나씩 순서대로 배열에 담기
			sortReady[i] = dao.list().get(i).getScore();
		}
		// 정렬할 배열의 시작 인덱스, 끝 인덱스, 배열값 넘기기
		m.mergeSort(0, sortReady.length - 1, sortReady);
		return sortReady;
	}

	public List<PointVO> uIDSearch(int score) {
		return dao.uIDSearch(score);
	}

	public List<PointVO> point() {
		List<PointVO> list = dao.quick(); // 정렬안된 리스트
		List<PointVO> pointList = new ArrayList<PointVO>();
		QuickCreate q = new QuickCreate();
		String user = null;
		int[] score = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			score[i] = list.get(i).getScore(); // 점수들을 가져와서 배열에 넣음
		}
		q.quicksort(score); // 배열을 정렬함
		for (int i = 0; i < score.length; i++) {
			// 같은 스코어일 경우 DAO에서 2개 이상 가져오게 됨
			if (dao.find(score[i]).size() >= 2) {
				for (int j = 0; j < dao.find(score[i]).size(); j++) {
					PointVO vo = new PointVO();
					user = dao.find(score[i]).get(j).getuID(); // 가져온 값들로 db에서 userid를 찾아옴
					vo.setuID(user); // 찾아온 userid vo에 넣음
					vo.setScore(score[i]); // userid의 점수를 vo에 넣음
					pointList.add(vo); // vo를 넘겨줄 리스트에게 add
				}
				i = i + dao.find(score[i]).size()-1;
			} else {
				PointVO vo = new PointVO();
				user = dao.find(score[i]).get(0).getuID();
				vo.setuID(user);
				vo.setScore(score[i]);
				pointList.add(vo);
			}
		}
		return pointList;
	}

}
