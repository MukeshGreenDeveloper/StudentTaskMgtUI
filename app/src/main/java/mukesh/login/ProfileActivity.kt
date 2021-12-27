package mukesh.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import mukesh.login.chart.AAGradientColor
import mukesh.login.chart.AALinearGradientDirection
import mukesh.login.chart.aachartcreator.*
import mukesh.login.chart.chartcomposer.MixedChartComposer
import mukesh.login.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity(), AAChartView.AAChartViewCallBack {
    lateinit var binding: ActivityProfileBinding
    lateinit var vm: ChatVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(ChatVM::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.apply {
            lifecycleOwner = this@ProfileActivity
            viewModel = vm
        }
        /*vm.selectedModeMonth.observe(this, Observer {
            when(it){
                0 -> {}
                1->{}
                2->{}
            }
        })*/
        binding.txtWeek.setOnClickListener {
            vm.selectedModeMonth.value = 0
        }
        binding.txtMonth.setOnClickListener {
            vm.selectedModeMonth.value = 1
        }
        binding.txtAllTime.setOnClickListener {
            vm.selectedModeMonth.value = 2
        }
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        setUpAAChartView()
    }
    private var aaChartModel = AAChartModel()
    private var aaChartView: AAChartView? = null
    private fun setUpAAChartView() {
        aaChartView = findViewById(R.id.AAChartView)
        aaChartView?.setBackgroundColor(0)
        aaChartView?.callBack = this
        aaChartModel = configureAAChartModel()
        aaChartView?.aa_drawChartWithChartModel(aaChartModel)

    }


    private fun configureAAChartModel(): AAChartModel {
//        val intent = intent
        val chartType = AAChartType.Area.value/*intent.getStringExtra("chartType")*/
        val position = 2/*intent.getIntExtra("position", 0)*/
        val chartTypeEnum = convertStringToEnum(chartType!!)

        val aaChartModel = AAChartModel.Builder(this)
            .setChartType(chartTypeEnum)
            .setBackgroundColor("#4b2b7f")
            .setDataLabelsEnabled(false)
            .setYAxisGridLineWidth(0f)
            .setLegendEnabled(false)
            .setTouchEventEnabled(true)
            .setSeries(
                AASeriesElement()
                    .name("BigData")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("Chemistry")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                    .name("Maths")
                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                    .name("DataScience")
                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            )
            .build()

        configureTheStyleForDifferentTypeChart(chartType,position)

        return aaChartModel
    }

    private fun configureTheStyleForDifferentTypeChart(chartType: String, position: Int) {
        if ((chartType == AAChartType.Area.value || chartType == AAChartType.Line.value)
            && (position == 4 || position == 5)) {
            configureStepAreaChartAndStepLineChartStyle()
        } else if (chartType == AAChartType.Column.value || chartType == AAChartType.Bar.value) {
            configureColumnChartAndBarChartStyle()
        } else if (chartType == AAChartType.Area.value || chartType == AAChartType.Areaspline.value) {
            configureAreaChartAndAreasplineChartStyle(chartType)
        } else if (chartType == AAChartType.Line.value || chartType == AAChartType.Spline.value) {
            configureLineChartAndSplineChartStyle(chartType)
        }
    }

    private fun configureStepAreaChartAndStepLineChartStyle() {
        val element1 = AASeriesElement()
            .name("BigData")
            .step(true)
            .data(arrayOf(149.9, 171.5, 106.4, 129.2, 144.0, 176.0, 135.6, 188.5, 276.4, 214.1, 95.6, 54.4))

        val element2 = AASeriesElement()
            .name("Chemistry")
            .step(true)
            .data(arrayOf(83.6, 78.8, 188.5, 93.4, 106.0, 84.5, 105.0, 104.3, 131.2, 153.5, 226.6, 192.3))

        val element3 = AASeriesElement()
            .name("Maths")
            .step(true)
            .data(arrayOf(48.9, 38.8, 19.3, 41.4, 47.0, 28.3, 59.0, 69.6, 52.4, 65.2, 53.3, 72.2))

        aaChartModel?.series(arrayOf(element1, element2, element3))
    }

    private fun configureColumnChartAndBarChartStyle() {
        aaChartModel
            .categories(arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Spe", "Oct", "Nov", "Dec"))
            .colorsTheme(arrayOf("#fe117c", "#ffc069", "#06caf4", "#7dffc0"))
            .animationType(AAChartAnimationType.EaseInCubic)
            .animationDuration(1200)
    }

    private fun configureAreaChartAndAreasplineChartStyle(chartType:String) {
        aaChartModel
            .animationType(AAChartAnimationType.EaseOutQuart)
            .markerRadius(5f)
            .markerSymbol(AAChartSymbolType.Circle)
            .markerSymbolStyle(AAChartSymbolStyleType.InnerBlank)

        if (chartType == AAChartType.Areaspline.value) {
            val gradientColorDic = AAGradientColor.linearGradient(
                AALinearGradientDirection.ToBottomRight,
                "rgba(138,43,226,1)",
                "rgba(30,144,255,1)" //颜色字符串设置支持十六进制类型和 rgba 类型
            )

            val element1 = AASeriesElement()
                .name("Predefined symbol")
                .data(arrayOf(0.45, 0.43, 0.50, 0.55, 0.58, 0.62, 0.83, 0.39, 0.56, 0.67, 0.50, 0.34, 0.50, 0.67, 0.58, 0.29, 0.46, 0.23, 0.47, 0.46, 0.38, 0.56, 0.48, 0.36))

            val element2 = AASeriesElement()
                .name("Image symbol")
                .data(arrayOf(0.38, 0.31, 0.32, 0.32, 0.64, 0.66, 0.86, 0.47, 0.52, 0.75, 0.52, 0.56, 0.54, 0.60, 0.46, 0.63, 0.54, 0.51, 0.58, 0.64, 0.60, 0.45, 0.36, 0.67))

            val element3 = AASeriesElement()
                .name("Base64 symbol (*)")
                .data(arrayOf(0.46, 0.32, 0.53, 0.58, 0.86, 0.68, 0.85, 0.73, 0.69, 0.71, 0.91, 0.74, 0.60, 0.50, 0.39, 0.67, 0.55, 0.49, 0.65, 0.45, 0.64, 0.47, 0.63, 0.64))

            val element4 = AASeriesElement()
                .name("Custom symbol")
                .data(arrayOf(0.60, 0.51, 0.52, 0.53, 0.64, 0.84, 0.65, 0.68, 0.63, 0.47, 0.72, 0.60, 0.65, 0.74, 0.66, 0.65, 0.71, 0.59, 0.65, 0.77, 0.52, 0.53, 0.58, 0.53))


            aaChartModel
                .animationType(AAChartAnimationType.EaseFrom)//设置图表渲染动画类型为 EaseFrom
                .series(arrayOf(element1, element2, element3, element4))
        }
    }

    private fun configureLineChartAndSplineChartStyle(chartType: String) {
        aaChartModel
            .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)//设置折线连接点样式为:边缘白色
            .markerRadius(6f)
        if (chartType == AAChartType.Spline.value) {
            val element1 = AASeriesElement()
                .name("Chemistry")
                .lineWidth(7f)
                .data(arrayOf(50, 320, 230, 370, 230, 400))

            val element2 = AASeriesElement()
                .name("BigData")
                .lineWidth(7f)
                .data(arrayOf(80, 390, 210, 340, 240, 350))

            val element3 = AASeriesElement()
                .name("Maths")
                .lineWidth(7f)
                .data(arrayOf(100, 370, 180, 280, 260, 300))

            val element4 = AASeriesElement()
                .name("DataWarehouse")
                .lineWidth(7f)
                .data(arrayOf(130, 350, 160, 310, 250, 268))

            aaChartModel
                ?.animationType(AAChartAnimationType.SwingFromTo)
                ?.series(arrayOf(element1, element2, element3, element4))

        }
    }


    private fun convertStringToEnum(chartTypeStr: String): AAChartType {
        var chartTypeEnum = AAChartType.Column
        when (chartTypeStr) {
            AAChartType.Column.value -> chartTypeEnum = AAChartType.Column
            AAChartType.Bar.value -> chartTypeEnum = AAChartType.Bar
            AAChartType.Area.value -> chartTypeEnum = AAChartType.Area
            AAChartType.Areaspline.value -> chartTypeEnum = AAChartType.Areaspline
            AAChartType.Line.value -> chartTypeEnum = AAChartType.Line
            AAChartType.Spline.value -> chartTypeEnum = AAChartType.Spline
        }
        return chartTypeEnum
    }

    override fun chartViewDidFinishLoad(aaChartView: AAChartView) {
        println("🔥图表加载完成回调方法 ")
    }

    override fun chartViewMoveOverEventMessage(
        aaChartView: AAChartView,
        messageModel: AAMoveOverEventMessageModel
    ) {
        val gson = Gson()
        println("🚀move over event message " + gson.toJson(messageModel))
    }

}