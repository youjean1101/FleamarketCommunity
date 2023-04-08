package fleaMarket.a02_service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fleaMarket.a03_dao.Req4002_Dao;
import vo.BoardImg;
import vo.Capplication;
import vo.FollowMemberInfo;
import vo.PageJYJ;
import vo.RoomMemberInfo;

@Service
public class Req4002_Service {
	@Autowired(required=false)
	private Req4002_Dao dao;
	
	public void communityInsert(Capplication ins) {
		if(ins.getHashtag()==null) ins.setHashtag("");
		dao.communityInsert(ins);
	}
	
	public void communityFileInsert(BoardImg fIns) {
		dao.communityFileInsert(fIns);
	}
	
	public Capplication boardDetailSelect(int sel) {
		if(dao.boardDetailSelect(sel).getImgname()==null) dao.boardDetailSelect(sel).setImgname("");
		return dao.boardDetailSelect(sel);
	}
	
	public void communityUpdate(Capplication upt) {
		if(upt.getHashtag()==null) upt.setHashtag("");
		dao.communityUpdate(upt);
	}
	
	public void communityFileUpdate(BoardImg fupt) {
		if(fupt.getImgname()==null) fupt.setImgname("");
		dao.communityFileUpdate(fupt);
	}
	
	public List<FollowMemberInfo> followerSelect(FollowMemberInfo index) {
		if(index.getKeyword()==null) index.setKeyword("");
		if(index.getMyemail()==null) index.setMyemail("");
		return dao.followerSelect(index);
	}
	
	public List<FollowMemberInfo> followPage(PageJYJ sel){
		if(sel.getKeyword()==null) sel.setKeyword("");
		if(sel.getMyEmail()==null) sel.setMyEmail("");
		
		// 1. 총페이지 수
		sel.setCount(dao.followTotCnt(sel));
		// 2. 현재페이지 번호(클릭한)
		if(sel.getCurPage()==0) {
			sel.setCurPage(1);
		}
		// 3. 한페이지에 보일 데이터 갯수
		//   - 초기화면 현재 페이지 번호 0 ==> default설정
		if(sel.getPageSize()==0) {
			sel.setPageSize(10);
		}
		// 4. 총페이지 수.(전체데이터/한페이지에 보일 데이터 건수)
		//    한번에 보일 데이터 건수 5건일 때, 총건수11 ==> 3페이지
		//    100건?  100/5 ==> 20 page 필요
		//    101건?  101/5 ==> 21 page 필요(올림처리 필요)
		sel.setPageCount(
				(int)Math.ceil(
				sel.getCount()/(double)sel.getPageSize())
				);
		// 블럭의 [이후]에 대한 예외 처리..
		if(sel.getCurPage()>sel.getPageCount()) {
			sel.setCurPage(sel.getPageCount());
		}			
		
		// 5. 마지막 번호
		sel.setEnd(sel.getCurPage()*sel.getPageSize());
		sel.setStart((sel.getCurPage()-1)*sel.getPageSize()+1);
		// 6. 블럭처리
		//    1) 블럭 크기 지정
		sel.setBlockSize(10);
	
		
		//	  2) 블럭 번호 지정 : 현재페이지번호/블럭의 크기 올림 처리
		int blocknum = (int)Math.ceil(sel.getCurPage()/
					(double)sel.getBlockSize());
		//    3) 마지막 블럭
		int endBlock = blocknum*sel.getBlockSize();
		if(endBlock>sel.getPageCount()) {
			endBlock = sel.getPageCount();
		}
		sel.setEndBlock(endBlock);
		//	  4) 시작 블럭
		if(blocknum!=0)
			sel.setStartBlock((blocknum-1)*sel.getBlockSize()+1);
		
		return dao.followPage(sel);
	}
	
	public int followCheck(String myEmail, String following) {
		Map<String, String> selckMap = new HashMap<String, String>();
		selckMap.put("myEmail", myEmail);
		selckMap.put("following", following);
		return dao.followCheck(selckMap);
	}
	
	public void followmemberdelete(FollowMemberInfo del) {
		dao.followmemberdelete(del);
	}
	
	public int boardLikeCnt(String email){
		return dao.boardLikeCnt(email);
	}
	
	public List<RoomMemberInfo> roomMemberInfo(Map<String, String> map){
		return dao.roomMemberInfo(map);
	}
	
	public List<RoomMemberInfo> boardReplySelect(Map<String, String> map) {
		return dao.boardReplySelect(map);
	}
	
	public List<RoomMemberInfo> boardSelect(RoomMemberInfo sel){
		//if(sel.getImgname()==null) sel.setImgname("");
		if(sel.getCategory()==null) sel.setCategory("");
		if(sel.getEmail()==null) sel.setEmail("");
		if(sel.getRegistDateMonth()==null) sel.setRegistDateMonth("");
		return dao.boardSelect(sel);
	}
	
	public void insertFriend(String login,String follwing) {
		Map<String, String> followAddMap = new HashMap<String, String>();
		followAddMap.put("myEmail", login);
		followAddMap.put("following", follwing);
		dao.insertFriend(followAddMap);
	}
}
