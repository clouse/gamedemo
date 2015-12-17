package com.atet.tvmarket.control.task;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class CheckInTaskAdapter extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private List<CheckInItem> checkInItems = new ArrayList<CheckInItem>();
	public CheckInTaskAdapter(RecyclerView recyclerView){
		this.mRecyclerView = recyclerView;
	}
	
	public void setData(List<CheckInItem> checkInItems) {
		this.checkInItems.clear();
		this.checkInItems.addAll(checkInItems);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		
		return checkInItems.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
		CheckInItem item = checkInItems.get(position);
		ItemViewHolder holder = (ItemViewHolder) viewHolder;
		holder.setTaskKey(item.getItemkey());
		holder.setTaskValue(item.getItemValue());
		
		if(item.getTextSize()==1){
			holder.getTaskKey().setTextSize(TypedValue.COMPLEX_UNIT_PX,20);
		}
		if(item.getTextSize()==2){
			holder.getTaskKey().setTextSize(TypedValue.COMPLEX_UNIT_PX,14);
		}
		if(item.isReceived()){
			holder.setItemByTaskReceived();
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		View root = LayoutInflater.from(container.getContext()).inflate(R.layout.checkin_task_item, container, false);
		ScaleViewUtils.scaleView(root);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder{

		private TextView taskKey,taskValue;
		private ImageView received;
		public ItemViewHolder(View itemView) {
			super(itemView);
			taskKey = (TextView)itemView.findViewById(R.id.tv_task_key);
			taskValue = (TextView)itemView.findViewById(R.id.tv_task_value);
			received = (ImageView)itemView.findViewById(R.id.iv_received);
		}
		
		public void setTaskKey(CharSequence key){
			taskKey.setText(key);
		}
		
		public void setTaskValue(CharSequence value){
			taskValue.setText(value);
		}

		public TextView getTaskKey() {
			return taskKey;
		}
		
		public void setItemByTaskReceived(){
			received.setVisibility(View.VISIBLE);
		}
		
	}
}
