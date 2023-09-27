# MD5
MD5，即“Message-Digest Algorithm 5（信息-摘要算法）”,主要作用是用于验证文件或内容是否被篡改。

MD5是输入不定长度信息，输出固定长度128-bits的算法。经过程序流程，生成四个32位数据，最后联合起来成为一个128-bits散列。
基本方式为，求余、取余、调整长度、与链接变量进行循环运算。得出结果。

1、填充编码。

在MD5算法中，首先需要对信息进行填充，使其位长对512求余的结果等于448。
因此，信息的位长（Bits Length）将被扩展至N*512+448，N为一个非负整数，N可以是零。
填充的方法如下，在信息的后面填充一个1和无数个0，直到满足上面的条件时才停止用0对信息的填充。
然后，在这个结果后面附加一个以64位二进制表示的填充前信息长度。
经过这两步的处理，现在的信息的位长=N*512+448+64=(N+1）*512，即长度恰好是512的整数倍。
这样做的原因是为满足后面处理中对信息长度的要求。

# 对称加密
最常用的对称加密是AES,加密和解码使用同一个密钥。核心就是使用异或操作来实现加解密。

要理解AES的加密流程，会涉及到AES加密的五个关键词，分别是：分组密码体制、Padding、密钥、初始向量IV和四种加密模式

1. 分组密码体制

分组密码体制就是指将明文切成一段一段的来加密，然后再把一段一段的密文拼起来形成最终密文的加密方式。AES采用分组密码体制，
即AES加密会首先把明文切成一段一段的，而且每段数据的长度要求必须是128位（即16个字节），如果最后一段不够16个字节了，
就需要用Padding来把这段数据填满16个字节，然后分别对每段数据进行加密，最后再把每段加密数据拼起来形成最终的密文。

2. Padding填充模式
Padding就是用来把不满16个字节的分组数据填满16个字节用的，它有三种模式PKCS5、PKCS7和NOPADDING。

PKCS5是指分组数据缺少几个字节，就在数据的末尾填充几个字节的5。
PKCS7是指分组数据缺少几个字节，就在数据的末尾填充几个字节的0。
NoPadding是指不需要填充，也就是说数据的发送方肯定会保证最后一段数据也正好是16个字节。
原生jdk不支持PKCS7Padding填充方式，通过第三方BouncyCastle组件来让java里面支持PKCS7Padding填充。

3. 密钥
AES要求密钥的长度可以是128位16个字节、192位或者256位，位数越高，加密强度自然越大，但是加密的效率自然会低一些，
因此要做好衡量。我们开发通常采用128位16个字节的密钥，我们使用AES加密时需要主动提供密钥，而且只需要提供一个密钥就够了，
每段数据加密使用的都是这一个密钥，密钥来源为随机生成，只要保障密钥符合位数规则，以及解密和加密的密钥是同一个就行了。

4. 初始向量
初始向量IV的作用是使加密更加安全可靠，它可以实现同样的明文，只要初始向量不一样，输出的密文不一样。
我们使用AES加密时需要主动提供初始向量， 而且只需要提供一个初始向量就够了，后面每段数据的加密向量都是前面一段的密文。
初始向量IV的长度规定为128位16个字节，初始向量的来源为随机生成。


6. 四种加密模式
AES一共有四种加密模式，分别是ECB（电子密码本模式）、CBC（密码分组链接模式）、CFB、OFB，我们一般使用的是CBC模式。
四种模式中除了ECB相对不安全之外，其它三种模式的区别并没有那么大。

为什么ECB相对不安全？
因为ECB没有使用初始向量，相同的明文加密后，输出的密文内容是一样的，这种情况可以利用彩虹表的方式破解获取明文。
而CBC使用了初始向量，这就可以实现相同的明文加密后，输出的密文内容是不一样，那么攻击者很难破解获取明文信息。


# 非对称加密

非对称加密就是加密和解密使用的不是相同的密钥：只有同一个公钥-私钥对才能正常加解密。

非对称加密相比对称加密的显著优点在于，对称加密需要协商密钥，而非对称加密可以安全地公开各自的公钥，在N个人之间通信的时候：使用非对称加密只需要N个密钥对，每个人只管理自己的密钥对。而使用对称加密需要则需要`N*(N-1)/2`个密钥，因此每个人需要管理`N-1`个密钥，密钥管理难度大，而且非常容易泄漏。

非对称加密的缺点就是运算速度非常慢，比对称加密要慢很多。

非对称加密的典型算法就是RSA算法，RSA 加密算法基于一个十分简单的数论事实：**将两个大 素数 相乘十分容易，但想要对其乘积进行 因式分解 却极其困难，因此可以将 乘积 公开作为 加密密钥**。

**注意**：

`RSA加密内容是有长度限制的`，1024位密钥可以加密128字节（1024位），不满128字节的使用随机数填充，但是RSA实现中必须要加`随机数（11字节以上）`，所以`明文长度最大为117字节`。

| 步骤 | 说明         | 描述              | 备注                                     |
| :--- | :----------- | :---------------- | :--------------------------------------- |
| 1    | 找出质数     | P 、Q             | -                                        |
| 2    | 计算公共模数 | N = P * Q         | -                                        |
| 3    | 欧拉函数     | φ(N) = (P-1)(Q-1) | -                                        |
| 4    | 计算公钥E    | 1 < E < φ(N)      | E的取值必须是整数 E 和 φ(N) 必须是互质数 |
| 5    | 计算私钥D    | E * D % φ(N) = 1  | -                                        |
| 6    | 加密         | C ＝ M E mod N    | C：密文  M：明文                         |
| 7    | 解密         | M ＝C D mod N     | C：密文  M：明文                         |

公钥＝(E , N) 私钥＝(D, N)

对外，我们只暴露公钥。

## 示例

#### 1、找出质数 P 、Q

```javascript
P = 3  
Q = 11
```

#### 2、计算公共模数

```javascript
N = P * Q = 3 * 11 = 33
N = 33
```

#### 3、 欧拉函数

```javascript
φ(N) = (P-1)(Q-1) = 2 * 10 = 20
φ(N) = 20
```

#### 4、计算公钥E

```javascript
1 < E < φ(N)
1 <E < 20
```

E 的取值范围 {3, 7, 9, 11, 13, 17, 19} E的取值必须是整数, E 和 φ(N) 必须是互质数 为了测试，我们取最小的值 **E =3** 3 和 φ(N) =20 互为质数，满足条件

#### 5、计算私钥D

```javascript
E * D % φ(N) = 1
3 * D  % 20 = 1   
```

根据上面可计算出  **D = 7**

#### 6、公钥加密

我们这里为了演示，就加密一个比较小的数字  M = 2

>  公式：C ＝ ME mod N

```javascript
M = 2
E = 3
N = 33
```

>  C = 23 % 33 = 8

明文 **“2”** 经过 RSA 加密后变成了密文 **“8”**

#### 7、私钥解密

M ＝CD mod N

```javascript
C = 8
D = 7
N = 33
```

>  M = 87 % 33 8 * 8 * 8 * 8 * 8 * 8 * 8=2097152 8 * 8 * 8 * 8 * 8 * 8 * 8 % 33 = 2

密文 **“8”** 经过 RSA 解密后变成了明文 2。


# 测试
```
        String testString = "lgy hello qeee!";
        Cipher cipher = new MessageDigest2(MessageDigest.MD5);
        Log.e("qwe","MD5:"+cipher.encode(testString));
        cipher = new MessageDigest2(MessageDigest.SHA1);
        Log.e("qwe","MD5:"+cipher.encode(testString));
        cipher = new MessageDigest2(MessageDigest.SHA256);
        Log.e("qwe","MD5:"+cipher.encode(testString));
        cipher = new MessageDigest2(MessageDigest.SHA512);
        Log.e("qwe","MD5:"+cipher.encode(testString));

        Cipher<String,String> cipher2 = new MessageDigest2(MessageDigest.MD5);
        String key = cipher2.encode("1234 qwe");
        byte[] iv = AES_CBC.generateIVParam();
        Log.e("qwe","key len:"+key.getBytes(StandardCharsets.UTF_8).length);
        cipher = new AES_CBC(AES_CBC.PKCS5Padding,key,iv);
        String encodeStr = (String)cipher.encode(testString.getBytes(StandardCharsets.UTF_8));
        Log.e("qwe","AES_CBC encode:"+encodeStr);
        String decodeStr = new String((byte[]) cipher.decode(encodeStr),StandardCharsets.UTF_8);
        Log.e("qwe","AES_CBC decode:"+decodeStr);

        Cipher<String,String> cipher3 = new MessageDigest2(MessageDigest.MD5);
        String key2 = cipher3.encode("1234 qwe");
        cipher = new AES_ECB(AES_ECB.PKCS5Padding,key2);
        String encodeStr2 = (String)cipher.encode(testString.getBytes(StandardCharsets.UTF_8));
        Log.e("qwe","AES_ECB encode:"+encodeStr2);
        String decodeStr2 = new String((byte[])cipher.decode(encodeStr2),StandardCharsets.UTF_8);
        Log.e("qwe","AES_ECB decode:"+decodeStr2);

        String[] keyPair = RSA.generateKey();
        cipher = new RSA(keyPair[0], keyPair[1]);
        byte[]  encodeByte = (byte[])cipher.encode(testString.getBytes(StandardCharsets.UTF_8));
        String encodeStr3 = Base64.encodeToString(encodeByte,Base64.NO_WRAP);
        Log.e("qwe","RSA encode:"+encodeStr3);
        byte[]  decodeByte = (byte[])cipher.decode(Base64.decode(encodeStr3,Base64.NO_WRAP));
        String decodeStr3 = new String(decodeByte,StandardCharsets.UTF_8);
        Log.e("qwe","RSA decode:"+decodeStr3);
```