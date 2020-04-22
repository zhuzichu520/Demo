package com.netease.nim.demo.nim.attachment;

import com.alibaba.fastjson.JSONObject;
import com.hiwitech.android.libs.tool.ToolFile;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class StickerAttachment extends NimAttachment {

    private final String KEY_CATALOG = "catalog";
    private final String KEY_CHARTLET = "chartlet";

    private String catalog;
    private String chartlet;

    public StickerAttachment() {
        super(NimAttachmentType.Sticker);
    }

    public StickerAttachment(String catalog, String emotion) {
        this();
        this.catalog = catalog;
        this.chartlet = ToolFile.getFileNameNoEx(emotion);
    }

    @Override
    protected void parseData(JSONObject data) {
        this.catalog = data.getString(KEY_CATALOG);
        this.chartlet = data.getString(KEY_CHARTLET);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_CATALOG, catalog);
        data.put(KEY_CHARTLET, chartlet);
        return data;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getChartlet() {
        return chartlet;
    }
}
