package com.movision.mybatis.votingrecords.service;


import com.movision.mybatis.votingrecords.entity.Votingrecords;
import com.movision.mybatis.votingrecords.mapper.VotingrecordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhurui
 * @Date 2017/8/30 10:17
 */
@Service
@Transactional
public class VotingrecordsService {
    private static Logger log = LoggerFactory.getLogger(VotingrecordsService.class);
    @Autowired
    private VotingrecordsMapper votingrecordsMapper;

    public int insertSelective(Votingrecords votingrecords) {
        try {
            log.info("添加投票记录");
            return votingrecordsMapper.insertSelective(votingrecords);
        } catch (Exception e) {
            log.error("添加投票记录失败", e);
            throw e;
        }
    }

    /**
     * 有没有透过
     *
     * @param name
     * @return
     */
    public int queryHave(String name) {
        try {
            log.info("有没有透过");
            return votingrecordsMapper.queryHave(name);
        } catch (Exception e) {
            log.error("有没有透过失败", e);
            throw e;
        }
    }

}