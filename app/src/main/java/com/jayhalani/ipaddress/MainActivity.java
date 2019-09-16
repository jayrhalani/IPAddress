package com.jayhalani.ipaddress;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.jayhalani.ipaddress.databinding.ActivityMainBinding;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        if (activeNetwork != null) {
            mBinding.ivIpAddress.setVisibility(View.VISIBLE);
            mBinding.tvIpAddress.setVisibility(View.VISIBLE);
            mBinding.tvIpAddress.setText(getLocalIpAddress());
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                mBinding.tvConnectionType.setText(getResources().getString(R.string.wifi));
                mBinding.ivConnectionIcon.setImageResource(R.drawable.ic_signal_wifi);
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                mBinding.tvConnectionType.setText(getResources().getString(R.string.mobile_data));
                mBinding.ivConnectionIcon.setImageResource(R.drawable.ic_signal_cellular);
            }
        } else {
            mBinding.tvIpAddress.setVisibility(View.GONE);
            mBinding.ivIpAddress.setVisibility(View.GONE);
            mBinding.tvConnectionType.setText(getResources().getString(R.string.not_connected));
            mBinding.ivConnectionIcon.setImageResource(R.drawable.ic_signal_cellular_off);
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
