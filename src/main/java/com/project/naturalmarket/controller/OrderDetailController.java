package com.project.naturalmarket.controller;

import com.project.naturalmarket.dto.OrderDetailDto;
import com.project.naturalmarket.service.OrderDetailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/sales")
@RestController
public class OrderDetailController {

    private OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<List<OrderDetailDto>> createOrderDetail(@Valid @RequestBody List<OrderDetailDto> orderDetailDto,
                                                                  @PathVariable(name = "userId") long userId){

        List<OrderDetailDto> orderDetail=orderDetailService.createOrderDetail(orderDetailDto,userId);

        return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderDetailDto>> getAllOrderDetail(){

        List<OrderDetailDto> orderDetailDtos=orderDetailService.getAll();

        return new ResponseEntity<>(orderDetailDtos,HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailDto>> getByOrderId(@PathVariable(name = "orderId")long orderId){
        List<OrderDetailDto> orderDetailDtos= orderDetailService.getByOrderId(orderId);

        return new ResponseEntity<>(orderDetailDtos,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDto> updateOrderDetail(@Valid @RequestBody OrderDetailDto orderDetailDto,
                                                            @PathVariable(name = "id")long id){

        OrderDetailDto orderDetailDto1=orderDetailService.update(orderDetailDto,id);
        return new ResponseEntity<>(orderDetailDto1,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable(name = "id")long id){
        orderDetailService.delete(id);
        return ResponseEntity.ok("Se elimino correctamente");
    }

}
