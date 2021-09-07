# HideMe
## 概要

Minecraft サーバー（Spigot）用のプラグインです。

`/hideme hide` コマンドを実行すると他のプレーヤーから見えない「隠れ状態」に出来ます。
隠れ状態のプレーヤーは他のプレーヤーから見えなくなり、ログイン・ログアウトメッセージも表示されなくなります。

また、ping パケットを書き換えることでプレーヤー数も偽装します。


## 使い方

| コマンド | パーミッション | デフォルト | 内容 |
| --- | --- | --- | --- |
| `/hideme <command>` | `hideme.command` | true | `hideme` コマンドの実行権限 |
| `/hideme version` | `hideme.version` | op | バージョンを表示する |
| `/hideme list` | `hideme.list` | op | 隠れ状態のプレーヤーの一覧を表示する |
| `/hideme status` | `hideme.status` | op | 自分の状態を表示する |
| `/hideme hide` | `hideme.hide` | op | 自分を隠れ状態にする |
| `/hideme hide <player>` | `hideme.hide.player` | op | 指定したプレーヤーを隠れ状態にする |
| `/hideme show` | `hideme.show` | op | 自分の隠れ状態を解除する |
| `/hideme show <player>` | `hideme.show.player` | op | 指定したプレーヤーの隠れ状態を解除する |
| `/hideme reapply` | `hideme.reapply` | op | プレーヤーの状態を設定しなおす |
| --- | `hideme.bypass` | op | 隠れ状態の効果をバイパスする |


## 注意

進捗達成のメッセージや他のプラグインの挙動によって、隠れ状態のプレーヤーの存在が明らかになってしまう場合があることに注意してください。
