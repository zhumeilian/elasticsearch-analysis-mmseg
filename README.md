配置：

mmseg_complex:
        type: mmseg
        seg_type: complex
        redis:
          pool:
            maxactive: 20
            maxidle: 10
            maxwait: 100
            testonborrow: true
          ip: 127.0.0.1:6379
          channel: mmseg_term
添加词：
publish mmseg_term c:羊外婆
删除词：
publish mmseg_term d:羊外婆
自定义词典是：mmseg/words-my.dic

