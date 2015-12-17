package com.atet.tvmarket.control.home.entity;

import java.util.ArrayList;
import java.util.List;

public class NewRecommandGameInfo extends BaseGameInfo {
	
	private int gameId;
	private String gameName;
	private String gameCover;
	
	public NewRecommandGameInfo(){
		
	}

	public NewRecommandGameInfo(int gameId, String gameName, String gameCover) {
		super();
		this.gameId = gameId;
		this.gameName = gameName;
		this.gameCover = gameCover;
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
	
	public List<BaseGameInfo> getNewRecommandGameInfos(){
		List<BaseGameInfo> infos = new ArrayList<BaseGameInfo>();
		
		infos.add(new NewRecommandGameInfo(0, "愤怒的小鸟", ""));
		infos.add(new NewRecommandGameInfo(0, "德州扑克", ""));
		infos.add(new NewRecommandGameInfo(0, "西米斗地主", ""));
		infos.add(new NewRecommandGameInfo(0, "3D终极狂飙", ""));
		infos.add(new NewRecommandGameInfo(0, "丑小鸭", ""));
		infos.add(new NewRecommandGameInfo(0, "魔法拼拼乐", ""));
		
		return infos;
	}
	
}
