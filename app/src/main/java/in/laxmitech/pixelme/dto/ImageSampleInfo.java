package in.laxmitech.pixelme.dto;

import in.laxmitech.pixelme.adapter.ImageSampleAdapterItemType;

/**
 * Created by sc on 23/12/17.
 */

public class ImageSampleInfo {

    private int adapterItemType = ImageSampleAdapterItemType.SAMPLE_IMAGE;
    private int imgResourceId;

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }

    public int getAdapterItemType() {
        return adapterItemType;
    }

    public void setAdapterItemType(int adapterItemType) {
        this.adapterItemType = adapterItemType;
    }
}
