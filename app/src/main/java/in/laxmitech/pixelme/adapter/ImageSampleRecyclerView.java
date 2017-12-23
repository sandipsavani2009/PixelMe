package in.laxmitech.pixelme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import in.laxmitech.pixelme.R;
import in.laxmitech.pixelme.dto.ImageSampleInfo;

/**
 * Created by sc on 23/12/17.
 */

public class ImageSampleRecyclerView extends RecyclerView.Adapter<ImageSampleRecyclerView.ImageViewHolder> {

    private List<ImageSampleInfo> mImageList;
    private ImageSampleRecyclerAdapterListener mAdaterListener;

    public ImageSampleRecyclerView(List<ImageSampleInfo> images, ImageSampleRecyclerAdapterListener listener) {
        mImageList = images;
        mAdaterListener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_img_recycler_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        switch (position) {
            case 1:

                break;

            case 2:
                break;

            case 3:
                break;


        }
    }

    @Override
    public int getItemCount() {
        return (mImageList != null) ? mImageList.size() : 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgView);
        }
    }
}
