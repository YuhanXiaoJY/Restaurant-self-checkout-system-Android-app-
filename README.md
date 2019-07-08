# 餐厅自助结账系统(Android App)

**Team Member：肖宇晗， 潘熙**

## 1. 背景介绍

在食堂吃饭的时候，我们经常感觉排队结账的队伍非常长。尤其是每天中午还有很多外来学习的“成功人士”与学生、老师一起排队，这导致我们会浪费很多时间在结账上面。所以，我们就有了设计一套自助结账系统的想法。这个自助结账系统通过机器学习的算法，识别出我们购买的菜品的盘子，通过盘子的颜色、形状来计算价格，然后我们就可以自己进行结账。这样一来，用餐的学生、老师都可以节约时间，而且还可以减少食堂工作人员的劳动量。

在随后的调研中，我们发现，我们这套系统不仅适用于饭堂，还可以在许多商业场景中投入使用。其中最简单的例子便是回转寿司餐厅。我们发现，回转寿司餐厅的碟子一般都是大小相同，颜色不同的圆形碟子，这非常适合我们进行图像识别的工作。经过考虑之后，为了提高正确性和简化工作，我们决定就使用回转寿司的碟子作为我们的实验数据。

在这个项目中，我们为商家提供了一个app（称其为Yummy），在app中实现消费者自助结账与评论功能，减轻前台机器的压力，提高商家运转的容错率。

在Yummy中，我们实现了6个活动，封装了登录登出、卡片式浏览、滑动式菜单、二维码扫描、分享、调用相册和照相机、评论、视觉识别等功能，力求兼顾app的视觉效果、功能全面性与稳定性。

 

## 2. 开发环境

- IDE：Andriod Studio 3.4.1

- SDK: 
  -  编译所用SDK：28 （对应于Android 9.0）
  - 测试App所用运行环境的SDK：27（对应于Android 8.1，工具为小米平板4）
  - 运行时支持的SDK版本范围：27~28

- Java版本：11.0.2

- 需额外下载的库：
  - openCV 3.4.6（安卓版）：用于视觉识别
  - zxing-lib[[①\]](#_ftn1)：用于二维码扫描

 

## 3. app介绍（以活动为单位进行介绍）

共实现了6个活动

### 3.1 LoginActivity

该活动用于登录。

- **技术要点**：
  - 使用SharedPreferences在手机SD卡中实现可持久化存储，从而方便用户信息的存取，同时进行权限设定，其它任何app都无法访问存储内容。
  - 布局上，设置“Remember password”勾选框，如果勾选，则重新登录时自动填写用户名与密码；增设注册按钮，实现到注册活动的切换。

- **功能演示**：

  在手机内部SD卡中查询用户，若无该用户或用户名与密码不匹配，显示“Account or password is invalid”：

|                                                              |      |                                                              |      |
| ------------------------------------------------------------ | ---- | ------------------------------------------------------------ | ---- |
| ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4socrsl0lj30lq0zkjt9.jpg) |      | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sod16dvaj30m80zoq51.jpg) |      |
|                                                              |      |                                                              |      |

 

若用户登录信息合格，点击LOGIN，进入主活动MainActivity：

|      |                                                              |      |                                                              |
| ---- | ------------------------------------------------------------ | ---- | ------------------------------------------------------------ |
|      | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sod6xqa6j30hq0scgmz.jpg) |      | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sodbnt6ej30ho0s7diz.jpg) |



 若没有帐号，点击“CREATE AN ACCOUNT!”，跳转到注册活动SignupActivity。注册成功，则直接跳转至主活动：

|                                                              |                                                              |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sods73r9j30h90q90tx.jpg) | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sofch84ij30h60q5dip.jpg) |



### 3.2 SignupActivity

在3.1中已经提到，不再赘述。

 

### 3.3 MainActivity

这一部分是Yummy App的门面，应用了2014年Google I/O大会推出的Material Design界面设计技术，主要起到页面导航、菜品展示（可以换成广告）、提升视觉效果等作用。

- 技术要点

  - 顶部标题栏：使用Toolbar而非ActionBar，ActionBar只能显示于页面顶部且无法隐去，Toolbar则可以根据实际需要实现丰富的显示效果。

  - 顶部标题栏的分享按钮：在手机的程序之间进行调用。同时使用Intent.createChooser，方便用户选择需要分享给的目标。

![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4soe6kq5gj30ll04mdg8.jpg)

- 顶部标题栏的“扫一扫”按钮：调用相机，根据zxing-lib库提供的接口实现**二维码扫描**。

![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sofmcn4ij31220btq4q.jpg)

- 顶部标题栏的右菜单按钮：集成了4个按钮，点开才会显示，用于活动页面的跳转。

- 顶部标题栏的左菜单按钮：使用DrawerLayout以及Material Design中的NavigationView技术实现**滑动菜单**功能。点击后，主页面会滑进一个菜单窗口。

![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sofvdb3dj30hs0au0to.jpg)

- 使用CardView实现卡片式布局，辅以RecyclerView实现滚动窗口下的卡片式布局。

- 使用AppBarLayout对顶部标题栏Toolbar进行再封装，确保标题栏与滚动卡片布局的显示互不影响。

- 使用SwipeRefresh实现滚动式卡片布局的下拉刷新。

 

- 功能演示：（所显示的菜品是随机挑选的，可能会出现重复）

 下拉滚动窗口时，顶部标题栏自动隐藏；上拉窗口时，顶部标题栏又自动显现：

|      |                                                              |                                                              |      |
| ---- | :----------------------------------------------------------: | :----------------------------------------------------------: | ---- |
|      | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4soge6lrsj30gy0qm41f.jpg) | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sogm9kwrj30gu0qv41h.jpg) |      |
|      | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4soh8l82gj30hn0u0whn.jpg) | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sohgz9laj30h90tyad4.jpg) |      |

  

 下拉刷新：

|                                                              |                                                              |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4sohx80jtj30he0q3q5z.jpg) | ![](http://ww1.sinaimg.cn/large/0071tMo1ly1g4soi695snj30hb0qb77c.jpg) |



刷新后：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4soieelwpj30gy0r4ju7.jpg)



 分享Yummy：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sojtbc7rj30gp0qq40t.jpg)



分享到QQ-我的电脑

 

|                                                              |                                                              |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sok15ontj30gy0r4wez.jpg) | ![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sok8quftj30gw0r1gm9.jpg) |

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sokethz8j30ia0t9wg1.jpg)



 扫描二维码：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4soklynk1j30hf0rttb3.jpg)




 顶部标题栏的右菜单：（点击可跳往各个页面）

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sokqlxt1j30gw0r1q5p.jpg)



 滑动菜单：（个人信息栏）

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sokw24txj30j50unq3p.jpg)




### 3.4 DishActivity

在主活动页面点击任意菜品，可进入属于该菜品的单独展示页。用于详细介绍菜品与提升视觉效果。

- 技术要点：

  - 使用RecyclerView来对滚动事件进行隐藏和显示。

  - 使用CollapsingToolbar将菜肴的背景图片与顶部标题栏相融合，充分利用系统状态栏空间，极大提升视觉效果。

  - 增设悬浮按钮。该按钮可随窗口的滚动而相应滚动乃至消失。

- 功能演示：

进入时的界面：（点击主活动页面的任意一个菜肴，这里以“樱花卷”为例）

背景图与顶部标题栏相融合，绿白环浮动按钮，对菜肴的陈述（置于卡片中，这里的陈述是计算机自动生成的）。

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sol0kd3dj30xc1hcx1q.jpg)

向上滑动（动态过渡）：

|                                                              |                                                              |                                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4solah3lbj30cz0lwwiz.jpg) | ![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4solkjtgvj30xc1hc4j8.jpg) | ![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4solwc25tj30xc1hcwz8.jpg) |

 

### 3.5 ShoppingCartActivity

对指定区域中的菜品进行拍照（或从相册中选取照片），通过视觉识别自动计算所需费用，

提供付款按钮进行付款跳转。

- 技术要点：

  - 调用照相机：需要获得**外部存储权限、调用摄像头的权限、自动聚焦**的权限，然后对摄像头进行调用；拍照前预设图片存储路径。
  - 从相册中选取图片：需要动态申请WRITE_EXTERNAL_STORAGE权限，由于相册的照片都存在SD卡上，只有**动态申请该权限**才能访问照片；同时，为了**兼容**新老版本的安卓手机或平板，获取图片也需要分情况考虑。如果是Android4.4以下版本，直接通过Uri就可以获取图片的真实路径；如果是Android4.4以上版本，还需要判断图片的Uri是什么类型，对document类型和content类型的Uri进行不同的操作，才能获取图片真实路径。
  - 视觉识别代码：在前面已经详细讲述了。

  - 一键付款：点击付款按钮，即可跳转至支付宝进行支付。想要实现直接进入付款页面的功能，需要在支付宝官方平台进行商家认证与一大堆注册，所以这里只能跳转至支付宝主页面，而不能直接付款。

- 功能演示：
初始页面：（三个按钮；一个用于显示照片的卡片；两行文字，表示图片中各类盘子的数目）

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4som8fejfj30i20rvq3r.jpg)



点击TAKE PHOTO，调用照相机：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sooc4d3fj30go0ocjtn.jpg)


点击CHOOSE FROM ALBUM，从相册中选取图片，并计算图片中的菜肴应该付多少费用。  

 盘子的计数与付款按钮中显示的应付费用随图片的改变而动态更新：

|      |                                                              |      |                                                              |
| ---- | ------------------------------------------------------------ | ---- | ------------------------------------------------------------ |
|      | ![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4somespu4j30ik0vtdhw.jpg) |      | ![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4soohit7fj30ik0vq0v2.jpg) |
|      |                                                              |      |                                                              |



  点击付款，进入支付宝页面：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sooojsmsj30do0ltgn5.jpg)


### 3.6 CommentActivity

用于对Yummy进行评论。采用聊天室布局。

- 技术要点

  - 使用RecyclerView实现对评论记录的滑动式浏览。

  - 使用SharedPreferences实现对评论记录的持久化存储，同时进行权限设定，其它任何app都无法访问存储内容。
  - 交互式聊天室窗口

- 功能演示

客户进入时的初始界面（显示已有的评论人+对应的评论内容）：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sopvx2laj30hi0s0t9h.jpg)

  

 客户可点击下方输入框发表评论（这里假设客户的账户名为admin，输入“不错不错”）：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4soqxj88ij30hr0sc3zc.jpg)



点击SEND：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4soqcbjtaj30fl0oxgm9.jpg)



离开本页面，再次进入，可以看到，该评论已被存储：

![](http://ww1.sinaimg.cn/mw690/0071tMo1ly1g4sor4qfclj30ip0tvq3u.jpg)

## 4. 改进空间

实现了以上的功能之后，我们认为，在以下的几个方面仍可对Yummy进行改进：

- 我们的程序中使用SharedPreferences进行持久化存储，同时也兼顾了安全性。但将某些信息存储在用户手机中是不合适的。因此，我们计划在存储方面进行改进，实现服务器端的代码，将用户的帐号信息以及评论内容存储到服务器端，维护SQL数据库来实现快速查询。

- 在菜肴的独立展示也面，我们已经布置了一个浮动的评论按钮，但限于时间没有进行深层次实现。我们打算除了提供对Yummy这个app的评论功能以外，还支持对每个菜肴的独立评论功能，使得客户能够根据他人评论更容易地挑选自己所喜欢的菜肴。

- 对app进行正式签名，在支付宝和微信官方平台注册商家信息，并进行相应的支付接口调用，从而能够在付款时实现一键付款。

- 接入广告SDK，在主活动页面动态布置广告。

------

[[①\]](#_ftnref1) <https://github.com/ahuyangdong/QrCodeLib/tree/master/zxing-lib>