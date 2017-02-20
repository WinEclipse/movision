package com.movision.mybatis.bossOrders.mapper;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.area.entity.Area;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.invoice.entity.Invoice;
import com.movision.mybatis.province.entity.Province;
import com.movision.mybatis.user.entity.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BossOrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossOrders record);

    int insertSelective(BossOrders record);

    BossOrders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BossOrders record);

    int updateByPrimaryKey(BossOrders record);

    List<BossOrdersVo> findAllOrdersByList(RowBounds rowBounds);

    List<BossOrdersVo> findAllOrderByCondition(Map map, RowBounds rowBounds);

    List<BossOrdersVo> findAllAccuracyConditionByOrder(Map map);

    BossOrdersVo queryOrderParticulars(Integer ordernumber);

    List<Province> findAllProvinceName();//查询省名

    List<City> findAllCityName(Integer id);//查询市名

    List<Area> findAllAreaName(Integer id);//查询区名

    Integer findAllBoss();//查询所有订单

    BossOrders findAllPerInfo(Integer id);//基本信息

    Invoice findAllInvoiceInfo(Integer id);//发票信息

    List<Address> findAllGetInfo(Integer id);//收货人信息

    int deleteOrder(Integer id);//删除订单

    int updateInvoice(Invoice invoice);//编辑发票

    Invoice queryInvoice(Integer orderid);//返回发票信息

    int updateAddress(Address address);//编辑收货人信息

    BossOrders queryGet(Integer orderid);//返回

    List<Address> queryAddress(Integer orderid);//查询历史地址

    List<Goods> queryGoods(Integer id);//查询商品信息

    int updateEmail(User user);//修改邮箱


}