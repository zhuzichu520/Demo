package com.netease.nim.demo.nim.attachment;

import com.alibaba.fastjson.JSONObject;
import com.hiwitech.android.shared.global.AppGlobal;
import com.netease.nim.demo.R;

/**
 * Created by huangjun on 2015/7/28.
 */
public class RTSAttachment extends NimAttachment {

    private byte flag;

    public RTSAttachment() {
        super(NimAttachmentType.RTS);
    }

    public RTSAttachment(byte flag) {
        this();
        this.flag = flag;
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("flag", flag);
        return data;
    }

    @Override
    protected void parseData(JSONObject data) {
        flag = data.getByte("flag");
    }

    public byte getFlag() {
        return flag;
    }

    public String getContent() {
        return AppGlobal.INSTANCE.getContext().getString(getFlag() == 0 ? R.string.start_session_record : R.string
                .session_end_record);
    }
}
