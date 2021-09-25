package com.xe.handlerdemo2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View


class MainActivity: AppCompatActivity() {
    var myC: MyC = MyC()
    var mH2: Handler? = null
    var mH3: Handler? = null
    companion object {
        var HANDLE_MESSAGE: Int = 1
        var EXIT_THREAD: Int = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyT2().start()
        MyT3().start()
    }

    inner class MyH2(looper: Looper): Handler(looper) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what) {
                HANDLE_MESSAGE -> {
                    var s: String = msg.obj as String
                    Log.d("MainActivity","--MyH2--" + s)
                }
                EXIT_THREAD -> {
                    Looper.myLooper().quitSafely()
                }
            }
        }
    }
    inner class MyH3(looper: Looper): Handler(looper) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what) {
                HANDLE_MESSAGE -> {
                    var s: String = msg.obj as String
                    Log.d("MainActivity","--MyH3--" + s)
                }
                EXIT_THREAD -> {
                    Looper.myLooper().quitSafely()
                }
            }
        }
    }

    inner class MyT2: Thread() {
        override fun run() {
            super.run()
            Looper.prepare()
            mH2 = MyH2(Looper.myLooper())
            Looper.loop()
        }
    }
    inner class MyT3: Thread() {
        override fun run() {
            super.run()
            Looper.prepare()
            mH3 = MyH3(Looper.myLooper())
            Looper.loop()
        }
    }
    inner class MyT4: Thread() {
        override fun run() {
            super.run()
            var m2: Message = Message.obtain();
            m2.what = HANDLE_MESSAGE
            m2.obj = "222"
            var m3: Message = Message.obtain();
            m3.what = HANDLE_MESSAGE
            m3.obj = "333"
            mH2?.sendMessage(m2)
            mH3?.sendMessage(m3)
        }
    }

    fun startThread4() {
        MyT4().start()
    }

    override fun onDestroy() {
        super.onDestroy()
        quitSafely()
    }

    fun onClick(v: View) {
        when(v.id) {
            R.id.btn_1 -> {
                startThread1()
            }
            R.id.btn_2 -> {
                startThread4()
            }
        }
    }
    inner class MyC : Handler.Callback {
        override fun handleMessage(msg: Message?): Boolean {
            return true
        }
    }
    inner class MyT: Thread() {
        override fun run() {
            super.run()
//            Looper.prepare()
            var h: Handler = Handler(myC)
//            Looper.loop()
        }
    }

    fun startThread1() {
        var myT: MyT = MyT()
        myT.start()
    }

    fun quitSafely() {
        if (mH2 != null) {
            mH2!!.sendEmptyMessage(EXIT_THREAD)
        }
        if (mH3 != null) {
            mH3!!.sendEmptyMessage(EXIT_THREAD)
        }
    }
}