package waterhole.miner.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import java.util.List;

import waterhole.miner.core.temperature.ThermalInfoUtil;

import static waterhole.miner.core.utils.Preconditions.checkNotNull;

/**
 * 抽象Miner类. 如没有特殊处理，继承此类即可，否则可选择实现{@link CommonMinerInterface}
 *
 * @author kzw on 2018/03/15.
 */
public abstract class AbstractMiner implements CommonMinerInterface {

    // 上下文对象
    private Context mContext;

    // 挖矿回调
    private StateObserver mMineCallback;

    protected int topTemperature = -1;

    public CommonMinerInterface setMaxTemperature(int temperature) {
        this.topTemperature = temperature;
        return this;
    }

    public double getCurrentTemperature() {
        double maxTemperature = 0;
        try {
            List<String> thermalInfo = ThermalInfoUtil.getThermalInfo();
            maxTemperature = -1;
            for (String info : thermalInfo) {
                String temp = info.replaceAll("(\\d+).*", "$1").trim();
                if (TextUtils.isDigitsOnly(temp.replace(".", ""))) {
                    double dTemp = Double.parseDouble(temp);
                    if (maxTemperature < dTemp)
                        maxTemperature = dTemp;
                }
            }
            if (maxTemperature > 1000)
                maxTemperature /= 1000;
            if (maxTemperature > 100)
                maxTemperature /= 10;
        } catch (Exception e) {
            e.printStackTrace();
            return 40;
        }
        return maxTemperature;
    }

    @Override
    public CommonMinerInterface setContext(Context context) {
        mContext = context;
        startCallbackServer();
        registerReceiver();
        return this;
    }

    private void registerReceiver() {
        mContext.registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /* 创建广播接收器 */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
             */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                ThermalInfoUtil.batteryTemperature = String.valueOf(intent.getIntExtra("temperature", 0));  //电池温度
            }
        }
    };

    private void startCallbackServer() {
        final Intent callbackIntent = new Intent(mContext, CallbackService.class);
        mContext.startService(callbackIntent);
    }

    @Override
    public Context getContext() {
        if (mContext == null) throw new RuntimeException("Please call setContext(Context context) first");
        return mContext;
    }

    @Override
    public AbstractMiner setStateObserver(StateObserver stateObserver) {
        mMineCallback = stateObserver;
        CallbackService.setCallBack(stateObserver);
        return this;
    }

    @Override
    public StateObserver getStateObserver() {
        return mMineCallback;
    }

    protected void asserts() {
        checkNotNull(mMineCallback, "Context must be not Null");
        checkNotNull(mMineCallback, "MineCallback must be not Null");
    }
}
