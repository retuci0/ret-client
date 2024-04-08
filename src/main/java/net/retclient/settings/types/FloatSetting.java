package net.retclient.settings.types;

import java.util.function.Consumer;

import net.retclient.settings.Setting;

public class FloatSetting extends Setting<Float> {
	public final float min_value;
	public final float max_value;
	public final float step;

	public FloatSetting(String ID, String description, float default_value, float min_value, float max_value, float step) {
		super(ID, description, default_value);
		this.min_value = min_value;
		this.max_value = max_value;
		this.step = step;
		type = TYPE.FLOAT;
	}
	
	public FloatSetting(String ID, String displayName, String description, float default_value, float min_value, float max_value, float step) {
		super(ID, displayName, description, default_value);
		this.min_value = min_value;
		this.max_value = max_value;
		this.step = step;
		type = TYPE.FLOAT;
	}
	
	public FloatSetting(String ID, String description, float default_value, float min_value, float max_value, float step, Consumer<Float> onUpdate) {
		super(ID, description, default_value, onUpdate);
		this.min_value = min_value;
		this.max_value = max_value;
		this.step = step;
		type = TYPE.FLOAT;
	}

	/**
	 * Setter for the value. Includes rounding to the nearest "step".
	 */
	@Override
	public void setValue(Float value) {
		double newValue = Math.max(min_value, Math.min(max_value, value));
		int steps = (int) Math.round((newValue) / step);
		super.setValue(step * steps);
	}
	
	/**
	 * Checks whether or not a value is with this setting's valid range.
	 */
	@Override
	protected boolean isValueValid(Float value) {
		return value >= min_value && value <= max_value;
	}
}