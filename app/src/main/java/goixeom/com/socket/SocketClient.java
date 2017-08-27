package goixeom.com.socket;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.parser.Packet;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import goixeom.com.MainApplication;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.models.BookingTrip;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;


public class SocketClient extends Service {
    IBinder mBinder;
    OnListenResponse onListenRespone;
    Socket socket;
    public boolean isContinuteBooking = false;
    SPUtils mSetting = new SPUtils(Constants.SETTING);
    public static final String TAG = "socket";
    Notification notification;
//    int idDriverPending = -1;
//    CountDownTimer countDownSentNextDriver = new CountDownTimer(21000,1000) {
//        @Override
//        public void onTick(long l) {
//
//        }
//
//        @Override
//        public void onFinish() {
//
//        }
//    };
    BookingTrip bookingTrip;
    int idPromotion;
    public interface OnListenResponse {
        void onResponse(String str);
    }

    public class LocalBinder extends Binder {
        public SocketClient getServerInstance() {
            return SocketClient.this;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
        if(socket!=null)
        this.socket.disconnect();

        Intent broadcastIntent = new Intent("chayngam.restart");
        sendBroadcast(broadcastIntent);
    }

    public void onCreate() {
        super.onCreate();
        LogUtils.e("OnCreate");
        mBinder = new LocalBinder();
        initSocket();

    }

    public void updateNewImei(String imei, String id) {
        if (socket == null) return;
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("imei", imei);
            objJoin.put("u_id", id);
            this.socket.emit("update_imei", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];
                    LogUtils.d(object.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void rateDriver(int idDriver, int  rate) {
        if (socket == null) return;
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("vote", rate);
            objJoin.put("d_id", idDriver);
            this.socket.emit("vote", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];
                    LogUtils.d(object.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void cancleBooking(int bookingId) {

        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("t_id", bookingId);
            this.socket.emit("auto-cancel", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];
                    LogUtils.d(object.toString());
                }
            });
            LogUtils.e("send auto cancel to server id " + bookingId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateLatlng(double lat, double lng, int id) {
        if (socket == null) return;

        JSONObject objJoin = new JSONObject();
//        try {
//            objJoin.put("lat", lat);
//            objJoin.put("lng", lng);
//            objJoin.put("u_id", id);
//            this.socket.emit("get-driver", objJoin, new Ack() {
//                @Override
//                public void call(Object... args) {
//                    JSONObject object = (JSONObject) args[0];
//                    LogUtils.d(object.toString());
//                }
//            });
//            LogUtils.e("get-driver ------------------------------------");
////            socket.on("abc", new Emitter.Listener() {
////                @Override
////                public void call(Object... args) {
////                    JSONObject object = (JSONObject) args[0];
////                    Intent intent = new Intent();
////                    intent.setAction(SocketConstants.SOCKET_RECIEVER_DRIVER);
////                    intent.putExtra(Constants.DRIVER, "acb");
////                    sendBroadcast(intent);
////                    LogUtils.e("dirverrrrrrrrrrrrrrrrrrrrrrrr" + object.toString());
////                }
////            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void creatBooking(final String start, final String end, final int price, final int idPromotion, final double distance, final String duration, BookingTrip Booking) {
//        if (socket == null || bookingTrip == null || bookingTrip.getList_driver() == null ||  bookingTrip.getList_driver().size() ==0 ||  bookingTrip.getList_driver().get(0) ==null) return;

        if (!mSetting.getString(Constants.ID).isEmpty() && socket != null && !MainApplication.getInstance().isTimeOut()) {
            this.bookingTrip = Booking;
            LogUtils.e("so luong tai xe " + bookingTrip.getList_driver().size());
            JSONObject objJoin = new JSONObject();
            if (bookingTrip.getList_driver().size() == 0) {
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.NEED_TO_REQUEST_LIST_DRIVER, true);
                SocketClient.this.sendBroadcast(intent);
                return;
            }
            try {
                objJoin.put("url", "/webservice/book-trip.php");
                objJoin.put("key", ApiConstants.API_KEY);
                objJoin.put("id", (mSetting.getString(Constants.ID)));
                objJoin.put("end", end);
                objJoin.put("start", start);
                objJoin.put("price", price);
                objJoin.put("idPromotion", idPromotion);
                objJoin.put("distance", distance);
                objJoin.put("duration", duration);
                objJoin.put("idBooking", bookingTrip.getT_id());
                objJoin.put("d_id", bookingTrip.getList_driver().get(0).getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.socket.emit("booking", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
//                    LogUtils.e("Noti toi tai xe - " + bookingTrip.getList_driver().get(0).getName() + bookingTrip.getList_driver().get(0).getName());
                    JSONObject object = (JSONObject) args[0];
                    AckSocket ackSocket = new Gson().fromJson(object.toString(), AckSocket.class);
                    if (ackSocket != null && ackSocket.getStatusCode() == 200 &&bookingTrip.getList_driver().size()>0) {
                        LogUtils.e("Thanh cong  " + bookingTrip.getList_driver().size());
                        isContinuteBooking = true;
                        Intent intent = new Intent();
                        intent.setAction(SocketConstants.EVENT_CONNECTION);
                        MainApplication.getInstance().setmDriver(bookingTrip.getList_driver().get(0).getName());
                        intent.putExtra(Constants.SEND_TO_DRIVER, bookingTrip.getList_driver().get(0).getName());
                        SocketClient.this.sendBroadcast(intent);
//                        idDriverPending = bookingTrip.getList_driver().get(0).getId();
//                        countDownSentNextDriver.start();
                    } else {
                        //khong thanh cong hoac tai xe khong chap nhan
                        LogUtils.e("khong thanh cong hoac tai xe khong chap nhan");
                        pushNotifyToNextDriver(start, end, price, idPromotion, distance, duration);
                    }
                }
            });


        }
    }

    public void listenToRoomBooking(final String start, final String end, final int price, final int idPromotion, final double distance, final String duration) {
        socket.off("received_"+bookingTrip.getT_id());

        socket.on("received_" + bookingTrip.getT_id(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                LogUtils.e("noti from booking    received_" + object.toString());
                SocketResponse response = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (response != null && mSetting.getString(Constants.ID) != null && response.status == SocketConstants.STATUS_DRIVER_CANCLE) {
                    // driver cancel booking
                    LogUtils.e("User cancler push to next user now ");
                    pushNotifyToNextDriver(start, end, price, idPromotion, distance, duration);
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.BOOKING, object.toString());
                SocketClient.this.sendBroadcast(intent);
            }
        });
    }
    public void reconnectlistenToRoomBooking(int id) {
        socket.off("received_"+id);

        socket.on("received_" + id, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                LogUtils.e("noti from booking    received_" + object.toString());
                SocketResponse response = new Gson().fromJson(object.toString(), SocketResponse.class);
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.BOOKING, object.toString());
                SocketClient.this.sendBroadcast(intent);
            }
        });
    }
    private void pushNotifyToNextDriver(String start, String end, int price, int idPromotion, double distance, String duration) {
        if (bookingTrip.getList_driver().size() > 0) {
            bookingTrip.getList_driver().remove(0);
            creatBooking(start, end, price, idPromotion, distance, duration, bookingTrip);
        } else {
            Intent intent = new Intent();
            intent.setAction(SocketConstants.EVENT_CONNECTION);
            intent.putExtra(Constants.NEED_TO_REQUEST_LIST_DRIVER, true);
            SocketClient.this.sendBroadcast(intent);
        }
    }

    public void feedback(String feedbackContent) {
        if (socket == null) return;
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("id", (mSetting.getString(Constants.ID)));
            objJoin.put("content", feedbackContent);
            this.socket.emit("feedback", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];
                    LogUtils.d(object.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public IBinder onBind(Intent intent) {
        LogUtils.e("onBind");
        initSocket();
        return this.mBinder;
    }

    public void showNotificationInStack() {
        if (notification != null && !isContinuteBooking) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(2, notification);
            notification = null;
        }
    }
    private void updateId(int id) {
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("u_id", id);
            this.socket.emit("user-login", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                }
            });
            LogUtils.e("Update id driver : " + id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void initSocket() {
        if (mSetting.getString(Constants.ID).isEmpty() || !NetworkUtils.isConnected() ) return;


        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
            }};
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            IO.setDefaultSSLContext(sc);
            IO.Options opts = new IO.Options();
            opts.secure = true;
            opts.reconnection = true;

            opts.reconnectionDelay = 2000;
            opts.reconnectionAttempts = 5;
            opts.sslContext = sc;
            this.socket = IO.socket("https://goixeom.com:3001/", opts);
            this.socket.connect();
            LogUtils.e("Set Socket IO", "Socket IO Seting");
        } catch (Exception e) {
            LogUtils.e("Socket Problem", "Try cath", e);
        }
        socket.emit("test", "hi", new Ack() {
            @Override
            public void call(Object... args) {
                LogUtils.e(args);
            }
        });
        updateId(Integer.parseInt(mSetting.getString(Constants.ID)));
//        Socket finalSocket = this.socket;
        this.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.e("Connected");
                final Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, getString(R.string.connected));
                SocketClient.this.sendBroadcast(intent);
                socket.off("event_profile");
                socket.on("event_profile", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject object = (JSONObject) args[0];
                        LogUtils.e(object.toString());
                        Intent intent = new Intent();
                        intent.setAction(SocketConstants.EVENT_CONNECTION);
                        intent.putExtra(Constants.PROFILE, object.toString());
                        SocketClient.this.sendBroadcast(intent);
                        SocketResponse response = new Gson().fromJson(object.toString(), SocketResponse.class);
                        if (response != null && !mSetting.getString(Constants.ID).isEmpty() && response.getIdUser() == Integer.parseInt(mSetting.getString(Constants.ID))) {
                            if (response.getImei() != null && !response.getImei().equals(PhoneUtils.getIMEI())) {
                                mSetting.put(Constants.PHONE, "");
                                mSetting.put(Constants.ID, "");
                                mSetting.put(Constants.TYPE_PASSWORD_MAP, false);
                                mSetting.put(Constants.TYPE_USER_STR, -1);
                                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
                                MainApplication.getInstance().setmUser(null);
                            }
                            return;
                        }
                    }
                });
                socket.on("connected", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        LogUtils.e("askjdbasdjbasjdabskd " + args[0].toString());

                    }
                });
                socket.off("send-notify");
                socket.on("send-notify", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        if (!mSetting.getBoolean(Constants.RECEIVE_NOTI, true) ) return;
                        JSONObject object = (JSONObject) args[0];
                        SocketResponse responseNotifcation = new Gson().fromJson(object.toString(), SocketResponse.class);
                        if (responseNotifcation != null && mSetting.getString(Constants.ID) != null && !mSetting.getString(Constants.ID).isEmpty()) {
                            if (responseNotifcation.getType() == Constants.TYPE_ALL_SYSTEM || responseNotifcation.getType() == Constants.TYPE_USER) {
                                if (responseNotifcation.getTypeDetail() == Constants.TYPE_ALL_USER || responseNotifcation.getTypeDetail() == Constants.TYPE_ALL || responseNotifcation.getTypeDetail() == mSetting.getInt(Constants.TYPE_USER_STR)) {
                                    if (responseNotifcation.getCategory() == Constants.PROMOTION_TYPE) {
                                        notification = CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.PROMOTION_TYPE);
                                        showNotificationInStack();
                                    } else {
                                        notification = CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.NOTIFICATION_NORMAL_TYPE);
                                        showNotificationInStack();
                                    }
                                }
                            }
                        }
                        LogUtils.e(object.toString());
                        Intent intent = new Intent();
                        intent.setAction(SocketConstants.EVENT_CONNECTION);
                        intent.putExtra(Constants.NOTIFICATION_SOCKET, object.toString());
                        SocketClient.this.sendBroadcast(intent);
                    }
                });
            }
        });


//
////        if (AppController.getInstance().getUser() != null) {
////            String token = SharedPref.getInstance(this).getString(Constant.KEY_TOKEN, BuildConfig.FLAVOR);
////            if (!token.isEmpty()) {
////                this.socket.on("notification_" + SharedPref.getInstance(getApplicationContext()).getString(Constant.KEY_ID, BuildConfig.FLAVOR), new C14569());
////            } else {
////                return START_STICKY;
////            }
////        }
        this.socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                Log.i("desc", "error desc");
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, getString(R.string.disconnect));
                SocketClient.this.sendBroadcast(intent);
//                if (SharedPref.getInstance(SocketClient.this.getApplicationContext()).getString(Constant.KEY_ORDER_ID, BuildConfig.FLAVOR).isEmpty()) {
//                    SharedPref.getInstance(SocketClient.this.getApplicationContext()).putBoolean(Constant.KEY_RECONNECTED, false);
//                } else {
//                    SharedPref.getInstance(SocketClient.this.getApplicationContext()).putBoolean(Constant.KEY_RECONNECTED, true);
//                }
            }
        });
        this.socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e("Error", args[0].toString());
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, getString(R.string.disconnect));
                // SocketClient.this.sendBroadcast(intent);
            }
        });
        this.socket.on(Packet.ERROR, new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e("Error", "Event Error");
                LogUtils.e(args[0].toString());
            }
        });
        this.socket.on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
            public void call(Object... args) {
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, SocketClient.this.getString(R.string.reconnect));
                //    SocketClient.this.sendBroadcast(intent);
                LogUtils.e(" reconecting ");
            }
        });
        this.socket.on("*", new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e("alllll" + args[0].toString());
            }
        });
        this.socket.on(Packet.MESSAGE, new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e(Packet.MESSAGE + args[0].toString());
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

    public BookingTrip getBookingTrip() {
        return bookingTrip;
    }

    public void setBookingTrip(BookingTrip bookingTrip) {
        this.bookingTrip = bookingTrip;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //create a intent that you want to start again..
        Intent intent = new Intent(getApplicationContext(), SocketClient.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }
}
