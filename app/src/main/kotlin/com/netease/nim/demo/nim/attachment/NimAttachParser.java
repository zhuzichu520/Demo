package com.netease.nim.demo.nim.attachment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

public class NimAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {
        NimAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            int type = object.getInteger(KEY_TYPE);
            JSONObject data = object.getJSONObject(KEY_DATA);
            switch (type) {
                case NimAttachmentType.Guess:
                    attachment = new GuessAttachment();
                    break;
                case NimAttachmentType.SnapChat:
                    return new SnapChatAttachment(data);
                case NimAttachmentType.Sticker:
                    attachment = new StickerAttachment();
                    break;
                case NimAttachmentType.RTS:
                    attachment = new RTSAttachment();
                    break;
                case NimAttachmentType.RedPacket:
                    attachment = new RedPacketAttachment();
                    break;
                case NimAttachmentType.OpenedRedPacket:
                    attachment = new RedPacketOpenedAttachment();
                    break;
                case NimAttachmentType.MultiRetweet:
                    attachment = new MultiRetweetAttachment();
                    break;
                default:
                    attachment = new DefaultCustomAttachment();
                    break;
            }

            if (attachment != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {

        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
