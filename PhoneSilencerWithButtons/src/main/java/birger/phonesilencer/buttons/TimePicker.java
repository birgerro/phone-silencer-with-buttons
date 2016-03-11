package birger.phonesilencer.buttons;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import birger.backports_api10.NumberPicker;

public class TimePicker extends FrameLayout implements NumberPicker.Formatter, NumberPicker.OnValueChangeListener {
	private NumberPicker hours_picker;
	private NumberPicker minutes_picker;
	
	public interface OnTimeChangedListener {
		public void onTimeChanged(TimePicker which, int hours, int minutes);
	}
	
	private OnTimeChangedListener registered_listener;
	
	private static final OnTimeChangedListener DO_NOTHING = new OnTimeChangedListener() {
		public void onTimeChanged(TimePicker which, int hours, int minutes) {
			// Do nothing
		}
	};
	
	public TimePicker(Context context) {
		this(context, null);
	}
	
	public TimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOnTimeChangedListener(DO_NOTHING);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.time_picker, this, true);
		
		// Get current time
		Calendar now = Calendar.getInstance();
		
        hours_picker = (NumberPicker) findViewById(R.id.time_hours);
        hours_picker.setMinValue(0);
        hours_picker.setMaxValue(23);
		hours_picker.setFormatter(this);
		hours_picker.setOnValueChangedListener(this);
        hours_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        hours_picker.setWrapSelectorWheel(true);
        
        hours_picker.setValue(now.get(Calendar.HOUR_OF_DAY));
        
        minutes_picker = (NumberPicker) findViewById(R.id.time_minutes);
        minutes_picker.setMinValue(0);
        minutes_picker.setMaxValue(59);
        minutes_picker.setFormatter(this);
		minutes_picker.setOnValueChangedListener(this);
        minutes_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutes_picker.setWrapSelectorWheel(true);

        minutes_picker.setValue(now.get(Calendar.MINUTE));
	}
	
	public void setOnTimeChangedListener(OnTimeChangedListener listener) {
		if ( listener != null ) {
			registered_listener = listener;
		} else {
			registered_listener = DO_NOTHING;
		}
	}

	public boolean is24HourView() {
		return true;
	}
	
	public void setIs24HourView(boolean ignored) {
		// do nothing
	}
	
	public int getCurrentHour() {
		return hours_picker.getValue();
	}
	
	public void setCurrentHour(int hour) {
		hours_picker.setValue(hour);
	}
	
	public int getCurrentMinute() {
		return minutes_picker.getValue();
	}
	
	public void setCurrentMinute(int minute) {
		minutes_picker.setValue(minute);
	}
	
	// For NumberPicker.Formatter interface:
    public String format(int value) {
    	// Format number with two digits:
		if ( value < 10 ) {
			return "0" + Integer.toString(value);
		} else {
			return Integer.toString(value);
		}
	}
    
    // For NumberPicker.OnValueChangeListener interface:
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		registered_listener.onTimeChanged(this, getCurrentHour(), getCurrentMinute());
	}
}
