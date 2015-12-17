package com.atet.tvmarket.control.home.entity;

import java.util.ArrayList;
import java.util.List;

public class RankingGameInfo extends BaseGameInfo {
	
	private int gameId;
	private String gameName;
	private String gameCover;
	private int rankingSeq;
	
	public RankingGameInfo(){
		
	}

	public RankingGameInfo(int gameId, String gameName, String gameCover,
			int rankingSeq) {
		super();
		this.gameId = gameId;
		this.gameName = gameName;
		this.gameCover = gameCover;
		this.rankingSeq = rankingSeq;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameCover() {
		return gameCover;
	}

	public void setGameCover(String gameCover) {
		this.gameCover = gameCover;
	}

	public int getRankingSeq() {
		return rankingSeq;
	}

	public void setRankingSeq(int rankingSeq) {
		this.rankingSeq = rankingSeq;
	}
	
	public List<BaseGameInfo> getDownloadRankingInfos(){
		List<BaseGameInfo> infos = new ArrayList<BaseGameInfo>();
		
		infos.add(new RankingGameInfo(0, "愤怒的小鸟", "", 1));
		infos.add(new RankingGameInfo(0, "德州扑克", "", 2));
		infos.add(new RankingGameInfo(0, "西米斗地主", "", 3));
		infos.add(new RankingGameInfo(0, "3D终极狂飙", "", 4));
		infos.add(new RankingGameInfo(0, "丑小鸭", "", 5));
		infos.add(new RankingGameInfo(0, "魔法拼拼乐", "", 6));
		
		return infos;
	}
	
	public List<BaseGameInfo> getRemoteControlRankingInfos(){
		List<BaseGameInfo> infos = new ArrayList<BaseGameInfo>();
		
		infos.add(new RankingGameInfo(0, "西米斗地主", "", 1));
		infos.add(new RankingGameInfo(0, "3D终极狂飙", "", 2));
		infos.add(new RankingGameInfo(0, "丑小鸭", "", 3));
		infos.add(new RankingGameInfo(0, "愤怒的小鸟", "", 4));
		infos.add(new RankingGameInfo(0, "德州扑克", "", 5));
		infos.add(new RankingGameInfo(0, "魔法拼拼乐", "", 6));
		
		return infos;
	}
	
	public List<BaseGameInfo> getHandleRankingInfos(){
		List<BaseGameInfo> infos = new ArrayList<BaseGameInfo>();
		
		infos.add(new RankingGameInfo(0, "3D终极狂飙", "", 1));
		infos.add(new RankingGameInfo(0, "丑小鸭", "", 2));
		infos.add(new RankingGameInfo(0, "魔法拼拼乐", "", 3));
		infos.add(new RankingGameInfo(0, "愤怒的小鸟", "", 4));
		infos.add(new RankingGameInfo(0, "德州扑克", "", 5));
		infos.add(new RankingGameInfo(0, "西米斗地主", "", 6));
		
		
		return infos;
	}
	
}
