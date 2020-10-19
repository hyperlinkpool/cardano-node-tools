package kr.hyperlinkpool.constants;

public enum StepOrder {
	STEP1(1)
	,STEP2(2)
	,STEP3(3)
	,STEP4(4)
	,STEP5(5)
	,EXIT(0)
	;
	
	private int stepOrder;
	
	StepOrder(int stepOrder) {
		this.stepOrder = stepOrder;
	}

	public int getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(int stepOrder) {
		this.stepOrder = stepOrder;
	}
	
	public static StepOrder getStepOrder(int stepOrder) {
		for(StepOrder value : StepOrder.values()) {
			if(value.getStepOrder() == stepOrder) {
				return value;
			}
		}
		return null;
	}
}
