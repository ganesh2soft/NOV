package com.hcl.springecomapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
	private Order order;

	@NotBlank
	@Size(min = 4, message = "Payment method must contain at least 4 characters")
	private String paymentMethod;
	private String pgName;
	private String pgPaymentId;
	private String pgStatus;
	private String pgResponseMessage;

	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Payment(Long paymentId, Order order,
			@NotBlank @Size(min = 4, message = "Payment method must contain at least 4 characters") String paymentMethod,
			String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
		super();
		this.paymentId = paymentId;
		this.order = order;
		this.paymentMethod = paymentMethod;
		this.pgName = pgName;
		this.pgPaymentId = pgPaymentId;
		this.pgStatus = pgStatus;
		this.pgResponseMessage = pgResponseMessage;
	}
	// TO get the payment information alone without order field
	public Payment(String paymentMethod, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
		this.paymentMethod = paymentMethod;
		this.pgPaymentId = pgPaymentId;
		this.pgStatus = pgStatus;
		this.pgResponseMessage = pgResponseMessage;
		this.pgName = pgName;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPgName() {
		return pgName;
	}

	public void setPgName(String pgName) {
		this.pgName = pgName;
	}

	public String getPgPaymentId() {
		return pgPaymentId;
	}

	public void setPgPaymentId(String pgPaymentId) {
		this.pgPaymentId = pgPaymentId;
	}

	public String getPgStatus() {
		return pgStatus;
	}

	public void setPgStatus(String pgStatus) {
		this.pgStatus = pgStatus;
	}

	public String getPgResponseMessage() {
		return pgResponseMessage;
	}

	public void setPgResponseMessage(String pgResponseMessage) {
		this.pgResponseMessage = pgResponseMessage;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", order=" + order + ", paymentMethod=" + paymentMethod + ", pgName="
				+ pgName + ", pgPaymentId=" + pgPaymentId + ", pgStatus=" + pgStatus + ", pgResponseMessage="
				+ pgResponseMessage + "]";
	}

}
