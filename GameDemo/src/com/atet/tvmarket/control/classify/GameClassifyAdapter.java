package com.atet.tvmarket.control.classify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout.LayoutParams;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameClassifyAdapter extends BaseAdapter{
	
	private RecyclerView recyclerView;
	private Context context;
	private ImageFetcher mImageFetcher;
	//private List<GameTypeInfo> gameTypeInfos = new ArrayList<GameTypeInfo>();
	private List<GameTypeInfo[]> arrayTypeInfos = new ArrayList<GameTypeInfo[]>();
	private List<GameInfo> games = new ArrayList<GameInfo>();
	private List<String> interfaceList = new ArrayList<String>();
	private GameClassifyRankingPanel rankingPanel;
	public enum ITEM_TYPE {
		ITEM_TYPE_ONE,
        ITEM_TYPE_TWO,
        ITEM_TYPE_THREE,
        ITEM_TYPE_FOUR,
        ITEM_TYPE_FIVE
    }
	
	public GameClassifyAdapter(Context context,RecyclerView recyclerView,ImageFetcher mImageFetcher,GameClassifyRankingPanel rankingPanel){
		this.context = context;
		this.mImageFetcher = mImageFetcher;
		this.recyclerView = recyclerView;
		this.rankingPanel = rankingPanel;
	}
	
	@Override
	public void onBindViewHolder(BaseViewHolder itemHolder, int position) {
		itemHolder.itemView.setTag(position);
		if(itemHolder.itemView instanceof GameClassifyRankingPanel){
			//GameClassifyRankingPanel rankingPanel = (GameClassifyRankingPanel) itemHolder.itemView;
			/*rankingPanel.setImageFetcher(mImageFetcher);
			rankingPanel.setData(games);*/
		}
		else if(itemHolder.itemView instanceof GameClassifyDoublePanel){
			GameClassifyDoublePanel doublePanel = (GameClassifyDoublePanel) itemHolder.itemView;
			doublePanel.getTopPanel().setNextFocusUpId(R.id.tab_categroy);
			if(arrayTypeInfos.size()!=0){
				GameTypeInfo[] gameTypeInfo = arrayTypeInfos.get(position-2);
				doublePanel.getTopName().setText(gameTypeInfo[0].getName());
				doublePanel.getTopName1().setText(gameTypeInfo[0].getName());
				doublePanel.setTopTypeId(gameTypeInfo[0].getTypeId());
				if(gameTypeInfo[0].getName().equals("遥控器专区")){
					doublePanel.getTopCover().setVisibility(View.VISIBLE);
					doublePanel.getTopCover().setImageResource(R.drawable.gameranking_control_icon1);
					mImageFetcher.loadImage("", doublePanel.getTopBg(), R.color.blue_bg);
					doublePanel.getTopName().setVisibility(View.INVISIBLE);
					doublePanel.getTopName1().setVisibility(View.VISIBLE);
				}
				else if(gameTypeInfo[0].getName().equals("手柄专区")){
					doublePanel.getTopCover().setVisibility(View.VISIBLE);
					doublePanel.getTopCover().setImageResource(R.drawable.gameranking_handle_icon1);
					mImageFetcher.loadImage("", doublePanel.getTopBg(), R.color.blue_bg);
					doublePanel.getTopName().setVisibility(View.INVISIBLE);
					doublePanel.getTopName1().setVisibility(View.VISIBLE);
				}
				else{
					doublePanel.getTopCover().setVisibility(View.INVISIBLE);
					mImageFetcher.loadImage(gameTypeInfo[0].getIcon(), doublePanel.getTopBg(), R.drawable.default_square);
				}
				
				if(interfaceList.contains(UpdateInterface.GAME_TYPE_DETAIL+gameTypeInfo[0].getTypeId())){
					doublePanel.getTop_new().setVisibility(View.VISIBLE);
				}
				else{
					doublePanel.getTop_new().setVisibility(View.INVISIBLE);
				}
				
				if(gameTypeInfo[1]!=null){
					doublePanel.getBottomName().setText(gameTypeInfo[1].getName());
					doublePanel.getBottomName1().setText(gameTypeInfo[1].getName());
					doublePanel.setBottomTypeId(gameTypeInfo[1].getTypeId());
					if(gameTypeInfo[1].getName().equals("遥控器专区")){
						doublePanel.getBottomCover().setVisibility(View.VISIBLE);
						doublePanel.getBottomCover().setImageResource(R.drawable.gameranking_control_icon1);
						mImageFetcher.loadImage("", doublePanel.getBottomBg(), R.color.blue_bg);
						doublePanel.getBottomName().setVisibility(View.INVISIBLE);
						doublePanel.getBottomName1().setVisibility(View.VISIBLE);
					}
					else if(gameTypeInfo[1].getName().equals("手柄专区")){
						doublePanel.getBottomCover().setVisibility(View.VISIBLE);
						doublePanel.getBottomCover().setImageResource(R.drawable.gameranking_handle_icon1);
						//doublePanel.getBottomBg().setImageResource(R.color.blue_bg);
						mImageFetcher.loadImage("", doublePanel.getBottomBg(), R.color.blue_bg);
						doublePanel.getBottomName().setVisibility(View.INVISIBLE);
						doublePanel.getBottomName1().setVisibility(View.VISIBLE);
					}
					else{
						doublePanel.getBottomCover().setVisibility(View.INVISIBLE);
						mImageFetcher.loadImage(gameTypeInfo[1].getIcon(), doublePanel.getBottomBg(), R.drawable.default_square);
					}
					
					if(interfaceList.contains(UpdateInterface.GAME_TYPE_DETAIL+gameTypeInfo[1].getTypeId())){
						doublePanel.getBottom_new().setVisibility(View.VISIBLE);
					}
					else{
						doublePanel.getBottom_new().setVisibility(View.INVISIBLE);
					}
				}
				else{
					doublePanel.getBottomPanel().setVisibility(View.GONE);
				}
				
			}
		}
		else if(itemHolder.itemView instanceof GameClassifySinglePanel){
			GameClassifySinglePanel singlePanel = (GameClassifySinglePanel) itemHolder.itemView;
			singlePanel.getLayout().setNextFocusUpId(R.id.tab_categroy);
			if(arrayTypeInfos.size()!=0){
				if(position>2){
					GameTypeInfo[] gameTypeInfo = arrayTypeInfos.get(position-2);
					Log.i("life", gameTypeInfo[0].toString());
					singlePanel.getName().setText(gameTypeInfo[0].getName());
					singlePanel.setTypeId(gameTypeInfo[0].getTypeId());
					if(gameTypeInfo[0].getName().replace(" ", "").equals("遥控器专区")){
						singlePanel.getIcon().setVisibility(View.VISIBLE);
						singlePanel.getIcon().setImageResource(R.drawable.gameranking_control_icon1);
						//singlePanel.getCover().setImageResource(R.color.blue_bg);
						mImageFetcher.loadImage("", singlePanel.getCover(), R.color.blue_bg);
					}
					else if(gameTypeInfo[0].getName().replace(" ", "").equals("手柄专区")){
						singlePanel.getIcon().setVisibility(View.VISIBLE);
						singlePanel.getIcon().setImageResource(R.drawable.gameranking_handle_icon1);
						mImageFetcher.loadImage("", singlePanel.getCover(), R.color.blue_bg);
					}
					else{
						singlePanel.getIcon().setVisibility(View.INVISIBLE);
						mImageFetcher.loadImage(gameTypeInfo[0].getIcon(), singlePanel.getCover(), R.drawable.default_vertical);
					}
					
					if(interfaceList.contains(UpdateInterface.GAME_TYPE_DETAIL+gameTypeInfo[0].getTypeId())){
						singlePanel.getNew_content().setVisibility(View.VISIBLE);
					}
					else{
						singlePanel.getNew_content().setVisibility(View.INVISIBLE);
					}
					
				}
				else{
					singlePanel.getIcon().setVisibility(View.INVISIBLE);
					singlePanel.getName().setText("专题游戏");
					mImageFetcher.loadLocalImage(R.drawable.topic_default_cover, singlePanel.getCover(), R.drawable.default_vertical);
					if(interfaceList.contains(UpdateInterface.GAME_TOPIC)){
						singlePanel.getNew_content().setVisibility(View.VISIBLE);
					}
					else{
						singlePanel.getNew_content().setVisibility(View.INVISIBLE);
					}
				}
			}
		}
		
		MarginLayoutParams layoutParams = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,MarginLayoutParams.WRAP_CONTENT);
		if(position==0){
			layoutParams.rightMargin=(int) ScaleViewUtils.resetTextSize(-40);
		}else if(position==getItemCount()-1){
			layoutParams.rightMargin=0;
		}
		else{
			layoutParams.rightMargin = (int) ScaleViewUtils.resetTextSize(-81);
			
		}
		
		itemHolder.itemView.setLayoutParams(layoutParams);
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		if(viewType==ITEM_TYPE.ITEM_TYPE_ONE.ordinal()){
			return new ItemHolder(rankingPanel);
		}
		else if(viewType==ITEM_TYPE.ITEM_TYPE_TWO.ordinal()){
			return new ItemHolder(new GameClassifySinglePanel(container.getContext()));
		}
		else if(viewType==ITEM_TYPE.ITEM_TYPE_THREE.ordinal()){
			return new ItemHolder(new GameClassifyDoublePanel(container.getContext()));
		}
		return null;
		/*else if(viewType==ITEM_TYPE.ITEM_TYPE_FOUR.ordinal()){
			return new ItemHolder(new GameClassifySinglePanel(container.getContext()));
		}
		else{
			return new ItemHolder(new GameClassifyDoublePanel(container.getContext()));
		}*/
        
	}
	
	class ItemHolder extends BaseViewHolder{

		public ItemHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
		}
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position==0){
			return ITEM_TYPE.ITEM_TYPE_ONE.ordinal();
		}
		else if(position>0&&position%2==1){
			return ITEM_TYPE.ITEM_TYPE_TWO.ordinal();
		}
		else if(position>0&&position%2==0){
			return ITEM_TYPE.ITEM_TYPE_THREE.ordinal();
		}
		return position;
		/*else if(position==3){
			return ITEM_TYPE.ITEM_TYPE_FOUR.ordinal();
		}
		else{
			return ITEM_TYPE.ITEM_TYPE_FIVE.ordinal();
		}*/
	}
	
	public void setData( List<GameTypeInfo> gameTypeInfos){
		/*this.gameTypeInfos.clear();
		this.gameTypeInfos.addAll(gameTypeInfos);*/
		Collections.sort(gameTypeInfos,comp);
		GameTypeInfo[] aTypeInfos=null;
		arrayTypeInfos.clear();
		while (gameTypeInfos.size()>0) {
			aTypeInfos = new GameTypeInfo[2];
			if(arrayTypeInfos.size()%2==0){
				if(gameTypeInfos.size()>=2){
					aTypeInfos[0] = gameTypeInfos.get(0);
					aTypeInfos[1]=gameTypeInfos.get(1);
					gameTypeInfos.remove(0);
					gameTypeInfos.remove(0);
				}
				else{
					aTypeInfos[0] = gameTypeInfos.get(0);
					aTypeInfos[1]=null;
					gameTypeInfos.remove(0);
				}
				
				arrayTypeInfos.add(aTypeInfos);
			}
			else{
				aTypeInfos[0] = gameTypeInfos.get(0);
				aTypeInfos[1]=null;
				gameTypeInfos.remove(0);
				arrayTypeInfos.add(aTypeInfos);
			}
		}
		
		notifyDataSetChanged();
	}
	
	public void checkNewContentInterface(List<String> interfaceList){
		this.interfaceList.clear();
		this.interfaceList.addAll(interfaceList);
	}
	
	@Override
	public int getItemCount() {
		if(arrayTypeInfos.size()==0){
			return 2;
		}
		return arrayTypeInfos.size()+2;
	}
	
	Comparator<GameTypeInfo> comp = new Comparator<GameTypeInfo>() {

		@Override
		public int compare(GameTypeInfo lhs, GameTypeInfo rhs) {
			return rhs.getOrderNum().compareTo(lhs.getOrderNum());
		}
	};
}
