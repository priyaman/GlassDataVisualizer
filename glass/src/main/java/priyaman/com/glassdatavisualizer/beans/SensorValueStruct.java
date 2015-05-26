package priyaman.com.glassdatavisualizer.beans;

import android.hardware.SensorManager;

import java.util.Arrays;


// Temporary (for demo).
// Bean class for storing the sensor data point...
public class SensorValueStruct
{
    private int type;
    private long timestamp;
    private float[] values;
    private int accuracy;

    public SensorValueStruct()
    {
        super();
    }
    public SensorValueStruct(int type, long timestamp, float[] values)
    {
        this(type, timestamp, values, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public SensorValueStruct(int type, long timestamp, float[] values, int accuracy)
    {
        super();
        this.type = type;
        this.timestamp = timestamp;
        this.values = values;
        this.accuracy = accuracy;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float[] getValues() {
        return values;
    }
    public void setValues(float[] values) {
        this.values = values;
    }

    public int getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
//        return "SensorValueStruct{" +
//                "type=" + type +
//                ", timestamp=" + timestamp +
//                ", values=" + Arrays.toString(values) +
//                ", accuracy=" + accuracy +
//                '}';
    }

}
