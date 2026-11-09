package com.waveclean.pro

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var rewardedAd: RewardedAd? = null
    private var isLoadingRewarded = false
    private var isShowingAd = false

    // Sizin Gerçek Google AdMob Ödüllü Reklam Birimi Kimliğiniz:
    private val REWARDED_AD_UNIT_ID = "ca-app-pub-8819233961749663/5113916136"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(AndroidAdMobBridge(), "AndroidAdMob")
        webView.loadUrl("file:///android_asset/index.html")

        // Resmi Google Mobile Ads SDK Hızlı Başlangıç Kılavuzu:
        // https://developers.google.com/admob/android/quick-start?hl=tr
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MainActivity) {
                runOnUiThread {
                    loadRewardedAd()
                }
            }
        }
    }

    private fun loadRewardedAd() {
        if (isLoadingRewarded || rewardedAd != null) return
        isLoadingRewarded = true
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this,
            REWARDED_AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    isLoadingRewarded = false
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isLoadingRewarded = false
                }
            }
        )
    }

    private fun showAdForEveryAction(actionTag: String) {
        if (isShowingAd) return
        val currentAd = rewardedAd
        if (currentAd == null) {
            loadRewardedAd()
            return
        }

        isShowingAd = true
        currentAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                isShowingAd = false
                rewardedAd = null
                loadRewardedAd() // Bir sonraki işlem için anında yeni reklam yükle
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                isShowingAd = false
                rewardedAd = null
                loadRewardedAd()
            }
        }

        currentAd.show(this, OnUserEarnedRewardListener {
            webView.evaluateJavascript("window.onAdMobRewardGranted && window.onAdMobRewardGranted('$actionTag');", null)
        })
    }

    inner class AndroidAdMobBridge {
        @JavascriptInterface
        fun triggerAdOnAction(actionTag: String) {
            runOnUiThread {
                showAdForEveryAction(actionTag)
            }
        }

        @JavascriptInterface
        fun showRewarded(modeName: String) {
            runOnUiThread {
                showAdForEveryAction(modeName)
            }
        }

        @JavascriptInterface
        fun showInterstitial() {
            runOnUiThread {
                showAdForEveryAction("cycle_complete")
            }
        }
    }
}
