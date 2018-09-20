package com.search.coupon.agent.ui.activity

import android.view.View
import com.search.coupon.agent.R
import com.search.coupon.agent.common.BaseActivity
import kotlinx.android.synthetic.main.activity_kotlin.*
/**
 * Created by song on 2018/1/17 0017.
 */
class KotlinActivity : BaseActivity() {
    override fun OnTopRightClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun OnTopLeftClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun setContentLayout() {
        setContentView(R.layout.activity_kotlin)
    }

    override fun dealLogicBeforeInitView() {
    }

    override fun initView() {
        textView.text = "kotlin"

    }

    override fun dealLogicAfterInitView() {





    }

    override fun onClickEvent(view: View?) {
    }

}
