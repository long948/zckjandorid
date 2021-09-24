package com.block.xjfkchain.data;

import java.util.List;

public class EarnResponse extends BaseResponse<EarnResponse.Bean> {

    public static class Bean {
        public List<EarningEntity> list;
        public List<EarningEntity> release;
        public String start;
        public String end;
        public String days;
    }
}
