package kr.po1.webtechmobile;

public class dialogButton
{
	private boolean useCancel = false;
	private boolean useClose = false;
	private boolean useNegative = false;
	private boolean usePositive = false;
	private boolean useConfirm = false;

	private String textCancel;
	private String textClose;
	private String textNegative;
	private String textPositive;
	private String textConfirm;

	public dialogButtonClickListener closeListener;
	public dialogButtonClickListener cancelListener;
	public dialogButtonClickListener negativeListener;
	public dialogButtonClickListener positiveListener;
	public dialogButtonClickListener confirmListener;

	public dialogButton()
	{
	}

	public boolean isUseClose()
	{
		return this.useClose;
	}

	public boolean isUseCancel()
	{
		return this.useCancel;
	}

	public boolean isUseNegative()
	{
		return this.useNegative;
	}

	public boolean isUsePositive()
	{
		return this.usePositive;
	}

	public boolean isUseConfirm()
	{
		return this.useConfirm;
	}

	public String getCloseText()
	{
		return this.textClose;
	}

	public String getCancelText()
	{
		return this.textCancel;
	}

	public String getNegativeText()
	{
		return this.textNegative;
	}

	public String getPositiveText()
	{
		return this.textPositive;
	}

	public String getConfirmText()
	{
		return this.textConfirm;
	}

	public dialogButton setCloseButton(String text, dialogButtonClickListener clickListener)
	{
		this.useClose = true;
		this.closeListener = clickListener;
		this.textClose = text;

		return this;
	}

	public dialogButton setCancelButton(String text, dialogButtonClickListener clickListener)
	{
		this.useCancel = true;
		this.cancelListener = clickListener;
		this.textCancel = text;

		return this;
	}

	public dialogButton setNegativeButton(String text, dialogButtonClickListener clickListener)
	{
		this.useNegative = true;
		this.negativeListener = clickListener;
		this.textNegative = text;

		return this;
	}

	public dialogButton setPositiveButton(String text, dialogButtonClickListener clickListener)
	{
		this.usePositive = true;
		this.positiveListener = clickListener;
		this.textPositive = text;

		return this;
	}

	public dialogButton setConfirmButton(String text, dialogButtonClickListener clickListener)
	{
		this.useConfirm = true;
		this.confirmListener = clickListener;
		this.textConfirm = text;

		return this;
	}

}
