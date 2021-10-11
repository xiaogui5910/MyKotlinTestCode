package com.example.chenchenggui.mykotlintestcode.activity.community

import com.example.chenchenggui.mykotlintestcode.MyApplication
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.CommunityDetailBean
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.CommunityDetailReplyTestBean
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.CommunityDetailTestBean
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.ImgCommonBean

/**
 * @description ：
 * @author : chenchenggui
 * @date: 7/15/21
 */
class CommunityDetailUtils {
    companion object {

        fun initRepliesTest(): ArrayList<CommunityDetailBean> {
            val testBean = generateTestData()
            val replyList = generateReplyTestData()
            val dataList = ArrayList<CommunityDetailBean>()

            val bean = CommunityDetailBean()
            bean.title = testBean.title
            bean.itemViewType = 1
            dataList.add(bean)

            val bean1 = CommunityDetailBean()
            bean1.owner = testBean.owner
            bean1.itemViewType = 2
            dataList.add(bean1)

            for (i in 0 until testBean.textList.size) {
                val bean2 = CommunityDetailBean()
                bean2.text = testBean.textList[i]
                bean2.itemViewType = 3
                dataList.add(bean2)
            }

            for (i in 0 until testBean.imgList.size) {
                val bean4 = CommunityDetailBean()
                bean4.img = testBean.imgList[i].imgurl
                bean4.imgType = if (i > 2) 1 else 0
                bean4.itemViewType = 4
                dataList.add(bean4)
            }

            val bean5 = CommunityDetailBean()
            bean5.isManager = testBean.isManager
            bean5.itemViewType = 5
            dataList.add(bean5)

            val bean6 = CommunityDetailBean()
            bean6.itemViewType = 6
            dataList.add(bean6)

            if (replyList.isEmpty()){
                val bean7 = CommunityDetailBean()
                bean7.itemViewType = 8
                dataList.add(bean7)
            }else{
                for (i in 0 until replyList.size) {
                    val bean7 = CommunityDetailBean()
                    bean7.replyTestBean = replyList[i]
                    bean7.itemViewType = 7
                    dataList.add(bean7)
                }
            }

            return dataList
        }

        fun generateTestData(): CommunityDetailTestBean {
            val testBean = CommunityDetailTestBean()
            testBean.textList = ArrayList<String>()
            testBean.textList.add(MyApplication.instance!!.getString(R.string.community_detail_text_default))
            testBean.textList.add(MyApplication.instance!!.getString(R.string.community_detail_text_default1))
            testBean.textList.add(MyApplication.instance!!.getString(R.string.community_detail_text_default2))

            testBean.imgList = ArrayList<ImgCommonBean>()
            val img = ImgCommonBean()
            img.imgurl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F2c.zol-img.com.cn%2Fproduct%2F124_500x2000%2F748%2FceZOdKgDAFsq2.jpg&refer=http%3A%2F%2F2c.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628843988&t=88b02e1dc3537c61d8b9944540deb823"
            testBean.imgList.add(img)
            val img1 = ImgCommonBean()
            img1.imgurl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic_360_360%2Fbb%2F37%2Ff5%2Fbb37f583e8da88aed385306a07361c2a.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628844027&t=5f9d9fcb6c33cfb769b09fa6515ab346"
            testBean.imgList.add(img1)
            val img2 = ImgCommonBean()
            img2.imgurl = "https://img2.baidu.com/it/u=3566088443,3713209594&fm=26&fmt=auto&gp=0.jpg"
            testBean.imgList.add(img2)
            val img3 = ImgCommonBean()
            img3.imgurl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdmimg.5054399.com%2Fallimg%2FNARUTOpicture%2Fguijiao%2F001.jpg&refer=http%3A%2F%2Fdmimg.5054399.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628844047&t=47a363dee671e71280a45e59bd78c1fd"
            testBean.imgList.add(img3)
            //gif
            val img4 = ImgCommonBean()
            img4.imgurl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180106%2F41cffa2741e0484c967e7cee7712edfa.gif&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628844083&t=5c943b30e1dcc4fed3d649237e18e364"
            img4.imgType = 1
            testBean.imgList.add(img4)

            val img5 = ImgCommonBean()
            img5.imgurl = "http://img.huofar.com/data/jiankangrenwu/shizi.gif"
            img5.imgType = 1
            testBean.imgList.add(img5)

            val img6 = ImgCommonBean()
            img6.imgurl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20190105%2F7b2a418575d348b0a52384d3fd76227d.gif&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628850515&t=5d2936e47beae6b454feef90da781034"
            img6.imgType = 1
            testBean.imgList.add(img6)

            val img7 = ImgCommonBean()
            img7.imgurl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F45fad97da4637b560ab0be8db1c99325a9fc7f99200c83-pPgZw7_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628850342&t=4196960526f56a454e2a0ba109b4446b"
            img7.imgType = 1
            testBean.imgList.add(img7)

            testBean.isManager = true
            testBean.replyCount = 999
            return testBean
        }

        fun generateReplyTestData(): ArrayList<CommunityDetailReplyTestBean> {
            val list = ArrayList<CommunityDetailReplyTestBean>()
            for (i in 0..20) {
                val replyTestBean = CommunityDetailReplyTestBean()
                if (i <= 5) {
                    replyTestBean.replyCount = 999
                }
                list.add(replyTestBean)
            }

            return list
        }
    }
}