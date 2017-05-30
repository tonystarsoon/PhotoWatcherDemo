package it.com.imagewatcherdemo.user;

import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import it.com.imagewatcherdemo.BaseFragment;
import it.com.imagewatcherdemo.BasePresenter;
import it.com.imagewatcherdemo.bean.PositionInfo;
import it.com.imagewatcherdemo.R;
import it.com.imagewatcherdemo.image.ShowActivity;
import it.com.imagewatcherdemo.bean.Image;
import it.com.imagewatcherdemo.net.NetWork;
import it.com.ninephotoview.NinePhotoView;

/**
 * 主界面的fragment
 * Created by tony on 2017/5/29.
 */
public class UserFragment extends BaseFragment implements Contract.IView {
    @BindView(R.id.ninelayout)
    NinePhotoView mNinelayout;
    private List<ImageView> mImageViewList = new ArrayList<>();
    private List<Image.ImageBean> mImageBeanList = new ArrayList<>();

    @Override
    public BasePresenter getPresenter() {
        return new UserPresenter(this);
    }

    public static BaseFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void initView() {
        mNinelayout.setOnItemClickListener(new NinePhotoView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mImageViewList.get(position);
                //单机条目的时候要执行的任务
                showDetail(position);
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user;
    }

    @Override
    public void showData(List<Image.ImageBean> list) {
        mImageBeanList.addAll(list);
        for (int index = 0; index < list.size(); index++) {
            ImageView imageView = mNinelayout.addPhotoView();
            mImageViewList.add(imageView);
            Glide.with(getContext())
                    .load(NetWork.BASE_URL + "show/image/" + list.get(index).getSubjectCover())
                    .centerCrop()
                    .into(imageView);
        }
        mNinelayout.showPhotoView();
    }

    private void showDetail(int position) {
        Intent intent = new Intent(getActivity(), ShowActivity.class);
        intent.putExtra("position", position);
        List<PositionInfo> positionInfoList = new ArrayList<>();
        int[] location = new int[2];
        for (int index = 0; index < mImageBeanList.size(); index++) {
            ImageView view = mImageViewList.get(index);
            view.getLocationOnScreen(location);
            PositionInfo positionInfo = new PositionInfo();
            positionInfo.setLocationX(location[0]);
            positionInfo.setLocationY(location[1]);
            positionInfo.setHeight(view.getHeight());
            positionInfo.setWidth(view.getWidth());
            positionInfo.setImageUrl(mImageBeanList.get(index).getSubjectCover());
            positionInfo.setImageTitle(mImageBeanList.get(index).getSubjectTitle());
            positionInfoList.add(positionInfo);
        }
        intent.putExtra("imageInfo", (Serializable) positionInfoList);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }
}
