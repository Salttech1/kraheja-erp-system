package kraheja.adminexp.billing.dataentry.invoiceCreation.mappers;
.trtype(invoicedetailEntity.getInvoicedetailCK().getInvdTrtype())
};

					.invdAcmajor(invoicedetailRequestBean.getAcmajor())
//	public static BiFunction<Invoicedetail, InvoicedetailRequestBean, Invoicedetail> updateInvoicedetailEntityPojoMapper = (invoicedetailEntity, invoicedetailRequestBean) -> {
//		invoicedetailEntity.setInvdAcmajor(Objects.nonNull(invoicedetailRequestBean.getAcmajor()) ? invoicedetailRequestBean.getAcmajor().trim() : invoicedetailEntity.getInvdAcmajor());