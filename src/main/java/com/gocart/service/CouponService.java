package com.gocart.service;

import com.gocart.entity.Coupon;
import com.gocart.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Optional<Coupon> getCouponByCode(String code) {
        return couponRepository.findByCode(code);
    }

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(String code, Coupon couponDetails) {
        return couponRepository.findByCode(code).map(coupon -> {
            if (couponDetails.getDescription() != null) {
                coupon.setDescription(couponDetails.getDescription());
            }
            if (couponDetails.getDiscount() != null) {
                coupon.setDiscount(couponDetails.getDiscount());
            }
            if (couponDetails.getForNewUser() != null) {
                coupon.setForNewUser(couponDetails.getForNewUser());
            }
            if (couponDetails.getForMember() != null) {
                coupon.setForMember(couponDetails.getForMember());
            }
            if (couponDetails.getIsPublic() != null) {
                coupon.setIsPublic(couponDetails.getIsPublic());
            }
            if (couponDetails.getExpiresAt() != null) {
                coupon.setExpiresAt(couponDetails.getExpiresAt());
            }
            return couponRepository.save(coupon);
        }).orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public void deleteCoupon(String code) {
        couponRepository.deleteById(code);
    }
}
