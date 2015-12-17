package com.atet.tvmarket.control.home.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameClassifyInfo extends BaseGameInfo implements Serializable{
	private int id;
	private String[] name;
	private String coverUrl;
	private int type;
	
	public GameClassifyInfo(){
		
	}
	
	public GameClassifyInfo(int id, String[] name, String coverUrl, int type) {
		super();
		this.id = id;
		this.name = name;
		this.coverUrl = coverUrl;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String[] getName() {
		return name;
	}
	public void setName(String[] name) {
		this.name = name;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public List<BaseGameInfo> getGameClassifyInfos(){
		List<BaseGameInfo> infos = new ArrayList<BaseGameInfo>();
		
		infos.add(new GameClassifyInfo(0, new String[]{"手柄专区","遥控器专区"}, "", 0));
		infos.add(new GameClassifyInfo(2, new String[]{"亲子游戏"}, "", 1));
		infos.add(new GameClassifyInfo(3, new String[]{"棋牌"}, "", 1));
		infos.add(new GameClassifyInfo(4, new String[]{"体育竞速"}, "", 1));
		infos.add(new GameClassifyInfo(5, new String[]{"射击跑酷"}, "", 1));
		infos.add(new GameClassifyInfo(6, new String[]{"单机游戏"}, "", 1));
		infos.add(new GameClassifyInfo(7, new String[]{"策略游戏"}, "", 1));
		infos.add(new GameClassifyInfo(8, new String[]{"数字游戏"}, "", 1));
		infos.add(new GameClassifyInfo(9, new String[]{"对战游戏"}, "", 1));
		
		return infos;
	}
}
