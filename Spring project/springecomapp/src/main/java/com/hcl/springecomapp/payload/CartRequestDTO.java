package com.hcl.springecomapp.payload;


public class CartRequestDTO {

    private Long userId;

    public CartRequestDTO() {
    }

    public CartRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

	@Override
	public String toString() {
		return "CartRequestDTO [userId=" + userId + "]";
	}

	
}
