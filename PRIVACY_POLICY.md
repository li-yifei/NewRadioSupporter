# NewRadioSupporter プライバシーポリシー

これは NewRadioSupporter（アプリケーションID：`io.github.takusan23.newradiosupporter`） のプライバシーポリシーです。

# かんたんに言うと

起動時と、必要になった際に要求する権限は、携帯電話の電波状態にアクセスするために使われ、それ以外の目的では利用しません。  
また、このアプリには取得した電波状態は端末内でのみ処理されます。そのため外部には送信しません、保存もしません。

# 1 収集する項目

- 電話の状態
- 位置情報

上記の情報にアクセスします。

# 2 利用箇所

- 2-1 アプリを起動したら電波状態を表示するため
- 2-2 ウィジェットで電波状態を表示するため
- 2-3 バックグラウンド状態でも通知領域から電波状態を表示するため

上記の箇所で電波状態を表示するため、1 で記載した情報へアクセスします。  
それ以外の目的には利用しません。

`2-3`に関しては、アプリがバックグラウンド状態でも（他のアプリを使っている間の事です）、通知領域に表示するためにバックグラウンド状態でも 1 で記載した情報へアクセスします。  
`2-3`を初めて利用する際に、追加でバックグラウンドで 1 で記載した情報へアクセスするための権限をリクエストします。

# 3 共有

共有しません。  
端末内で処理されるため、外部には送信しません。

## リクエストする権限

それぞれの権限の利用目的です。

### 機能に必要な権限

- ACCESS_NETWORK_STATE
  - ネットワークの状態にアクセスするために必要です。
  - 自動で権限が付与されます。
- ACCESS_FINE_LOCATION
  - 位置情報を取得する権限です。
  - 電波状態にアクセスする関数がこの権限を必要としているためです。
  - 電波状態にアクセスする目的にのみ使われます（位置情報の取得はしません）。
- READ_PHONE_STATE
  - 電話の状態にアクセスする権限です。
  - 電波状態にアクセスする目的で利用します。

### バックグラウンド 5G 通知機能を利用する場合

この機能を使わない場合は権限を拒否して大丈夫です。
 
- POST_NOTIFICATIONS
  - 通知を送信する権限です。
- ACCESS_BACKGROUND_LOCATION
  - バックグラウンドでも位置情報を取得する権限です。
  - バックグラウンド状態で電波状態にアクセスするためにはこちらの権限も必要です。
- FOREGROUND_SERVICE
  - フォアグラウンドサービス実行権限です。
  - こちらは自動で権限が付与されます。
- FOREGROUND_SERVICE_SPECIAL_USE
  - 特別な目的でフォアグラウンドサービスを使うための権限です。
  - 今回は電波状態にアクセスするため特別な目的にしています。
  - こちらは自動で権限が付与されます。