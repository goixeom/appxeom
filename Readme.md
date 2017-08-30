# Goi Xe Om - Customer
Kiến trúc : Java core - Android

Android studio >=2.3 

Gradle 2.3.3

Package Name : goixeom.com

## Thư viện hỗ trợ :
Tất cả thư viện đã liệt kê trong app/build.gradle .

+ Image : Picasso , Glide , Crop, CircleImage...
+ Kết nối RestApi : Retrofit2 & Gson
+ Socket : Socket IO client
+ Log/Utils/Toast... : utilcode
+ View : ButterKnife Binding view, Recyclerview pull to refresh



## Kiến trúc project : 
Các file class được chia theo từng loại trong từng folder. 
+ Activity : chưa các file activity class
+ fragment : chứa các file fragment
+ api : chưa các class liên quan đến việc kết nối webserver.
+ Views: các class custom view
+ Utils : Các class dùng lại ở nhiều project....
+ Socket : các class liên quan xử lý socket đặt chuyến, bắt chuyến, thông báo....
+ Models : các class models của cả project, 
## Chức Năng API
Công nghệ sử dụng Retrofit 

Đường dẫn chính : app/main/java/apis
+ ApiConstants : Chứa toàn bộ URL, CODE Response, STATUS CODE để kết nối RestApi
+ ApiUtils : File tạo kết nối đến Api của Restrofit
+ CallBackCustom/ CallBackCustomNoDelay : File Callback khi gọi Retrofit kết nối Api . Chuẩn hóa tất cả callback về 1 dạng inteface.
Tất cả callback sẽ chỉ trả về khi thành công và nếu lỗi xảy ra được sử lý trong file này.
+ PlaceDetailJson : Các file hỗ trợ parse Json từ google/place. 
+ ApiResponse : File class trả về chuẩn của mỗi api, Tất cả api trả về đều có dạng
```Json 
    {
     "data": {Json},
     "errorId": 200,
     "message": "OK"
     }
```
Tất cả data response sẽ tự động được cast về theo dạng ApiResponse.class bằng Gson. 
Chỉ cần định nghĩa ApiResponse<T> khi gọi callBack api.

#### Example
Gọi API Login : 
Url : ApiConstants.API_LOGIN
Method : GoiXeOmApi
```Java 
    @GET(ApiConstants.API_LOGIN)
            Call<ApiResponse<User>> login(@Query("key") String key, @Query("phone") String phone, @Query("password") String password, @Query("imei") String imei);
``` 
Tại đây đối tượng trả về là 1 User và được cast vào ApiResponse để tự động khi callbackCustom được gọi.

Gọi API :
```Java 
              Call<ApiResponse<User>> login = getmApi().login(ApiConstants.API_KEY, edtPhone.getText().toString(), password, PhoneUtils.getIMEI());
                    login.enqueue(new CallBackCustom<ApiResponse<User>>(LoginActivity.this, getDialogProgress(), new OnResponse<ApiResponse<User>>() {
                        @Override
                        public void onResponse(ApiResponse<User> response) {
                                // Xu ly khi response.getData() 
                         }
                           }));
                                }
                     });
``` 

## Socket IO Client 

Công nghệ sử dụng SocketIO java

Đường dẫn chính : app/main/java/socket

+ SocketClient : Là 1 service chạy ngầm, tự động kết nối tới socket và thực hiện.
+ AutoRestartService : Receiver tự động khởi động lại service SocketClient để socket luôn chạy cùng hệ thống kể cả thi tắt app
+ InternetReceiver :  Receiver tự động bắt sự kiện khi mất kết nối hoặc có kết nối internet
+ SocketConstants : class chứa toàn bộ url, trạng thái chuyến đi, chuỗi config với socket
+ SocketResponse : tương tự như class ApiResponse

#### Example :
Khởi tạo socket : SocketClient.initSocket()

Hàm này thực hiện việc khởi tạo socket và các trạng thái socket, Được gọi ngay khi service chạy

+ Để đẩy 1 gói tin lên server : getSocket().emit()..

+ Để lắng nghe 1 luồng sự kiện server bắn về : getSocket().on("Ten_Luong_Su_Kien)..

Cụ thể tham khảo API DOC.

## Trình tự hoạt động App Khách hàng
+ Splash :
    + Cần gọi api get config server để trả về nhưng config trên server.

+ Đăng ký/ Đăng Nhập:  
    + Khi đăng ký cần verify số điện thoại. Verify thành công thì mới tiếp tục đăng ký
    + Sau khi đăng nhập cần kiểm tra xem tài khoản đó đã đầy đủ thông tin chưa, nếu có đủ rồi thì vào map , ngược lại thì báo update.
    + Có thể đăng nhập bằng social, Khi đăng nhập social gửi id lên server kiểm tra . Nếu có thiếu thông tin thì bật màn hình update. Ngược lại thì vào map.
    + Tất cả đề phải verify số điện thoại mới được đăng ký hoặc update.
    + Sau khi đăng ký đăng nhập cần gọi socket update Imei máy điện thoại.
 + Map  : 
    + Cần gọi api lấy toàn bộ thông tin của khách hàng -> binding lên view.
    + Cần gọi socket lắng nghe luồng để nhận thông báo về notification, 
    + Tạo countdown để lấy về danh sách các xe xung quanh vị trí khách hàng. Hiển thị lên map các xe và di chuyển theo thời gian thực.
    + Khi có thay đổi vị trí các xe đã có trong danh sách cần di chuyển marker tương ứng.
 
 + Chọn Điểm đến : 
    + Khách hàng chọn điểm đến từ danh sách recently/favourite/nearby
    + Khách hàng có thể chọn điểm đến ngay trên bản đồ, hoặc search  1 địa điểm từ ô nhập
    + Các request lấy nearby, lấy tên địa chỉ từ location là request của google. 
    + Sau khi khác hàng tick favourite -> gọi api add vào list favourite
    + Sau khi 1 chuyến đi được đặt thì cũng gọi api add vào list recently
    
 + Đặt chuyến : 
    + Khi vào màn hình đặt chuyến cần gọi api get giá xăng, get direction google place, get giá tiền dựa vào khoảng cách 2 điểm.
    + Vẽ đường đi có animation chuyển động
    + Hiển thị các tài xế xung quanh tương tự bên map
    + Khách hàng có thể chọn mã khuyến mãi. Sau khi nhập mã cần tính lại số tiền và binding lại vào view hiển thị
 + Đặt chuyến  :
    + Trong 10s đầu khách hàng có thể hủy. Gọi countdown để hủy
    + Sau 10s nếu khách hàng không hủy thì Countdown gọi api get list danh sách tài xế xung quanh.
    + Thông tin chuyến sẽ được chuyển cho từng tài xế trong danh sách. Qua socket. gửi id tài xế và id chuyến đi lên server. Server sẽ tự điều hướng
    + Socket lắng nghe khi có tài xế hủy nhận chuyến thì tiếp tục gửi tài xế tiếp theo trong danh sách.
    + Chuyến đi được đặt tồn tại trong vòng 60s, Khi danh sách đã gửi hết mà không có tài xế nào nhận và vẫn trong 60s đặt chuyến thì tiếp tục gọi api get list danh sach và thực hiện lại các bước trên.
 + Khi có tài xế bắt chuyến thành công:
    + Cần hiển thị thông tin tài xế.
    + Lưu lại id chuyến đi cho việc reconnection.
    + Lắng nghe socket cập nhật vị trí tài xế trên bản đồ.
    + Mỗi khi nhận được vị trí của tài xế cần gọi api get direction google để vẽ đường đi và tính toàn khoảng cách , thời gian đến nơi
    + Lắng nghe các thay đổi của chuyến đi do tài xế cập nhật
    + Khi kết thúc cần hiện bảng đánh giá tài xế. Đánh giá thái độ của tài xế là cần thiết. 
    
    
 + Reconnection
    + Khi đang trong chuyến đi 
        + Gọi Api lấy về toàn bộ thông tin chuyến đang đi và binding view lại. 
        + Thực hiện kết nối join socket, lắng nghe location thay đổi  ,các trạng thái chuyến đi như trường hợp bắt chuyến thành công.
 + Notification 
    + Notification được đăng ký trong socketClient
    + Mỗi khi có noitification -> Gọi createNotification trong file CommomUtils và set các options cho notification để khi user mở notification sẽ tự động chuyển vào màn hình tương ứng
 + Các menu chức năng
    Gọi API tương ứng trong GoiXeOmApi -> BindingView
    
    

  


