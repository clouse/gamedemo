package com.atet.tvmarket.utils;

import com.atet.tvmarket.R;

import android.view.View;

public class IdUtils {
	public static boolean isIdVideoLayout(View v) {
		if (v == null) {
			return false;
		}
		if (v.getId() == R.id.setup_video_dialog_layout1
				|| v.getId() == R.id.setup_video_dialog_layout2
				|| v.getId() == R.id.setup_video_dialog_layout3) {
			return true;
		}
		return false;
	}

	public static boolean isIdChildlock(View v) {
		if (v == null) {
			return false;
		}
		if (v.getId() == R.id.childlock_modifypassword_btn
				|| v.getId() == R.id.childlock_setuppassword_sure_btn
				|| v.getId() == R.id.childlock_setuppassword_reset_btn
				|| v.getId() == R.id.childlock_confirmpassword_sure_btn
				|| v.getId() == R.id.childlock_newagepassword_sure_btn
				|| v.getId() == R.id.childlock_modifypassword_sure_btn
				|| v.getId() == R.id.childlock_modifypassword_reset_btn) {
			return true;
		}
		return false;
	}

	public static boolean isIdChildlockChoice(View v) {
		if (v == null) {
			return false;
		}
		if (v.getId() == R.id.setup_childlock_choice1_radbtn
				|| v.getId() == R.id.setup_childlock_choice2_radbtn
				|| v.getId() == R.id.setup_childlock_choice3_radbtn) {
			return true;
		}
		return false;
	}

	public static boolean isIdHandlelinkChoice(View v) {
		if (v == null) {
			return false;
		}
		if (v.getId() == R.id.setup_handlelink_dialog_layout1
				|| v.getId() == R.id.setup_handlelink_dialog_layout2
				|| v.getId() == R.id.setup_handlelink_dialog_layout3) {
			return true;
		}
		return false;
	}
}
