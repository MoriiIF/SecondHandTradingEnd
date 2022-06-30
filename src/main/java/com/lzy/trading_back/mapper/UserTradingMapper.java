package com.lzy.trading_back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzy.trading_back.entity.UserTradingRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTradingMapper extends BaseMapper<UserTradingRecord> {
}
