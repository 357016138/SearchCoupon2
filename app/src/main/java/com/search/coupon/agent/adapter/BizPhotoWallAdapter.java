package com.search.coupon.agent.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.search.coupon.agent.ui.fragment.BizPhotoWallFragment;

import java.util.ArrayList;

public class BizPhotoWallAdapter extends FragmentStatePagerAdapter {
	private ArrayList<String> mData;

	public BizPhotoWallAdapter(FragmentManager pFm, ArrayList<String> mData) {
		super(pFm);
		this.mData = mData;
	}

	@Override
	public BizPhotoWallFragment getItem(final int pPosition) {
		BizPhotoWallFragment _Fragment = new BizPhotoWallFragment();
		Bundle _Bundle = new Bundle();
		_Bundle.putString("url", mData.get(pPosition));
		_Fragment.setArguments(_Bundle);
		return _Fragment;
	}

	@Override
	public int getCount() {
		if(mData==null){
			return 0;
		}
		return mData.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
