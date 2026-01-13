package com.project.naturalmarket.service.impl;

import com.project.naturalmarket.dto.OrderDetailDto;
import com.project.naturalmarket.dto.OrderDto;
import com.project.naturalmarket.entity.OrderDetail;
import com.project.naturalmarket.entity.Orders;
import com.project.naturalmarket.entity.Product;
import com.project.naturalmarket.entity.User;
import com.project.naturalmarket.exception.MarketAPIException;
import com.project.naturalmarket.exception.ResourceNotFoundException;
import com.project.naturalmarket.repository.OrderDetailRepository;
import com.project.naturalmarket.repository.OrderRepository;
import com.project.naturalmarket.repository.ProductRepository;
import com.project.naturalmarket.repository.UserRepository;
import com.project.naturalmarket.service.OrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private OrderDetailRepository orderDetailRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    private UserRepository userRepository;
    private ModelMapper mapper;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository,
                                  OrderRepository orderRepository, ModelMapper mapper,
                                  ProductRepository productRepository,UserRepository userRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }

    @Override
    public List<OrderDetailDto> createOrderDetail(List<OrderDetailDto> orderDetailList, long userId) {
        //////////------------DESDE ACA SE CREAN ORDERDETAILS Y ORDER-----///////////////////


        // GENERAR PRIMERO EL ORDER EN LA BASE DE DATOS//
        OrderDto orderDto = new OrderDto();
        orderDto.setTotalProducts(orderDetailList.stream().mapToInt(OrderDetailDto::getTotalProduct).sum());


        //Convierte el Order creado de dto a entity
        Orders order1 = mapper.map(orderDto, Orders.class);

        //Busco y guardo el User con el userId
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        order1.setUser(user);

        //Convierto OrderDetail que se recibio en el metodo principal, de dto a una entidad para setear el Order
        List<OrderDetail> orderDetails = orderDetailList.stream().map(list -> mapper.map(list, OrderDetail.class)).toList();


        //Busco y guardo cada producto en cada OrderDetails
        List<Long> productIndex=orderDetailList.stream().map(OrderDetailDto::getProductId).collect(Collectors.toList());

        orderDetails.forEach(list2->{
            Product product=productRepository.findById(productIndex.get(0)).orElseThrow(
                    ()->new ResourceNotFoundException("Product","id",productIndex.get(0)));
            if (product.getStock()==0){
                throw new MarketAPIException(HttpStatus.BAD_REQUEST,"El Producto "+product.getName()+" no cuenta con stock");
            }
            list2.setProduct(product);
            list2.setTotalPrice(product.getPrice()*list2.getTotalProduct());
            productIndex.remove(0);
        });

        ////seteo el precioTotal en el Order
        List<Double> listaPrecios= orderDetails.stream().map(OrderDetail::getTotalPrice).toList();
        order1.setTotalPrice(listaPrecios.stream().mapToDouble(Double::doubleValue).sum());

        ///Guardo cada Order en cada OrderDetail
        orderDetails.forEach(list->list.setOrders(order1));

        orderRepository.save(order1);
        List<OrderDetail>orderDetails1= orderDetailRepository.saveAll(orderDetails);

        List<OrderDetailDto>orderDetailDtos=orderDetails1.stream().map(list-> mapper.map(list,OrderDetailDto.class)).collect(Collectors.toList());

        orderDetailDtos.forEach(list-> list.setOrderId(order1.getId()));
        return orderDetailDtos;
    }

    @Override
    public List<OrderDetailDto> getAll() {
        ///Pendiente de hacerlo paginado con spring data, que escuche que tenia una funcionalidad nueva
        List<OrderDetail> orderDetails=orderDetailRepository.findAll();

        return orderDetails.stream().map(order ->  mapper.map(order,OrderDetailDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailDto> getById(long id) {

        return null;
    }

    @Override
    public List<OrderDetailDto> getByOrderId(long id) {
        ///ESTA MAL, NO JALA DESDE LA BASE DE DATOS EN EL REPOSITORY
        Orders orders=orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Ordes","id",id));
        List<OrderDetail> orderDetails=orderDetailRepository.findOrderDetailsByOrderId(id);
        return orderDetails.stream().map(orderDetail -> mapper.map(orderDetail,OrderDetailDto.class)).collect(Collectors.toList());
    }

    @Override
    public OrderDetailDto update(OrderDetailDto orderDetailDto, long id) {
        /////FALTA PONER SI VINEN SIN ORDER O PRODUCT QUE PUEDA ACTULIZAR SIN PROBLEMAS
        OrderDetail orderDetail=orderDetailRepository.findById(id).orElseThrow(
                                ()-> new ResourceNotFoundException("OrdeDetail","id",id));

        Orders orders=orderRepository.findById(orderDetailDto.getOrderId()).orElseThrow(
                        ()->new ResourceNotFoundException("Ordes","id",orderDetailDto.getOrderId()));

        Product product=productRepository.findById(orderDetailDto.getProductId()).orElseThrow(
                                    ()-> new ResourceNotFoundException("Product","id",orderDetailDto.getProductId()));

        orderDetail.setTimestamp(orderDetailDto.getTimestamp());
        orderDetail.setTotalPrice(orderDetailDto.getTotalPrice());
        orderDetail.setTotalProduct(orderDetailDto.getTotalProduct());
        orderDetail.setOrders(orders);
        orderDetail.setProduct(product);

        orderDetailRepository.save(orderDetail);

        return mapper.map(orderDetail,OrderDetailDto.class);
    }

    @Override
    public void delete(long id) {
        OrderDetail orderDetail=orderDetailRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("OrdeDetail","id",id));

        orderDetailRepository.delete(orderDetail);

    }
}
