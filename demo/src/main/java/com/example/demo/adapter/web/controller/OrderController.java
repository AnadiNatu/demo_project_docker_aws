package com.example.demo.adapter.web.controller;

import com.example.demo.adapter.security.RestaurantUserDetails;
import com.example.demo.adapter.web.dto.OrderDto;
import com.example.demo.adapter.web.service.ActivityLogService;
import com.example.demo.adapter.web.service.OrderService;
import com.example.demo.domain.model.enums.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ActivityLogService activityLogService;

    // ── All authenticated roles ───────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<OrderDto.Response> create(
            @Valid @RequestBody OrderDto.CreateRequest request,
            @AuthenticationPrincipal RestaurantUserDetails principal,
            HttpServletRequest httpRequest) {

        String actor = principal.getUsername();
        OrderDto.Response response = orderService.create(request, actor);
        activityLogService.log(actor, principal.getUser().getRole(),
                "CREATE_ORDER", "ORDER", String.valueOf(response.getId()),
                "Order created | table=" + response.getTableNumber()
                        + " | total=₹" + response.getTotalAmount(), httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderDto.Response>> getMyOrders(
            @AuthenticationPrincipal RestaurantUserDetails principal) {
        return ResponseEntity.ok(orderService.getMyOrders(principal.getUsername()));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto.Response>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getByCustomer(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto.Response>> getByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto.Response> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderDto.UpdateStatusRequest request,
            @AuthenticationPrincipal RestaurantUserDetails principal,
            HttpServletRequest httpRequest) {

        String actor = principal.getUsername();
        OrderDto.Response response = orderService.updateStatus(id, request, actor);
        activityLogService.log(actor, principal.getUser().getRole(),
                "UPDATE_ORDER_STATUS", "ORDER", String.valueOf(id),
                "Order status -> " + request.getStatus(), httpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/bill")
    public ResponseEntity<OrderDto.BillResponse> generateBill(
            @PathVariable Long id,
            @AuthenticationPrincipal RestaurantUserDetails principal,
            HttpServletRequest httpRequest) {

        String actor = principal.getUsername();
        OrderDto.BillResponse bill = orderService.generateBill(id, actor);
        activityLogService.log(actor, principal.getUser().getRole(),
                "GENERATE_BILL", "ORDER", String.valueOf(id),
                "Bill generated | invoice=" + bill.getInvoiceNumber()
                        + " | total=₹" + bill.getTotalAmount(), httpRequest);
        return ResponseEntity.ok(bill);
    }

    // ── Admin-only ────────────────────────────────────────────────────────────

    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderDto.Response>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @DeleteMapping("/admin/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal RestaurantUserDetails principal,
            HttpServletRequest httpRequest) {

        String actor = principal.getUsername();
        orderService.cancel(id, actor);
        activityLogService.log(actor, principal.getUser().getRole(),
                "CANCEL_ORDER", "ORDER", String.valueOf(id),
                "Order cancelled by admin", httpRequest);
        return ResponseEntity.noContent().build();
    }

}