<template>
  <div class="map-container">
    <!-- 下拉菜单框 -->
    <el-row :gutter="20" style="margin-bottom: 10px;">
      <el-col :span="6">
        <el-select v-model="selectedProvince" placeholder="请选择省" filterable @change="handleProvinceChange">
          <el-option
              v-for="province in provinces"
              :key="province.value"
              :label="province.label"
              :value="province.value"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select v-model="selectedCity" placeholder="请选择市" filterable @change="handleCityChange">
          <el-option
              v-for="city in cities"
              :key="city.value"
              :label="city.label"
              :value="city.value"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select v-model="selectedDistrict" placeholder="请选择区" filterable @change="handleDistrictChange">
          <el-option
              v-for="district in districts"
              :key="district.value"
              :label="district.label"
              :value="district.value"
          ></el-option>
        </el-select>
      </el-col>
      <!-- 添加当前选择的地区信息和确认按钮 -->
      <el-col :span="6">
        <div style="display: flex; align-items: center;">
          <span style="margin-right: 10px;">
            当前选择：{{ selectedProvince }} {{ selectedCity }} {{ selectedDistrict }}
          </span>
          <el-button type="primary" @click="confirmLocation">确认</el-button>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索框和按钮 -->
    <el-row :gutter="20" style="margin-bottom: 10px;">
      <el-col :span="18">
        <el-input v-model="searchAddress" placeholder="请输入地址进行搜索"></el-input>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="searchLocation">搜索</el-button>
      </el-col>
    </el-row>

    <!-- 温度、天气、交通状况信息 -->
    <el-row :gutter="20" style="margin-bottom: 10px;">
      <el-col :span="8">
        <el-card>
          <div class="info-item">
            <i class="el-icon-sunny"></i>
            <span>温度：{{ currentWeather.temperature }}°C</span>
            <span class="future-info">未来3小时：{{ currentWeather.futureTemperature }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="info-item">
            <i class="el-icon-cloudy"></i>
            <span>天气：{{ currentWeather.condition }}</span>
            <span class="future-info">未来3小时：{{ currentWeather.futureCondition }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="info-item">
            <i class="el-icon-warning"></i>
            <span>交通状况：{{ currentTraffic.status }}</span>
            <el-button type="text" @click="showTrafficDetails" class="traffic-detail-btn">查看拥堵道路</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时地图 -->
    <div id="map-container" style="height: 550px; width: 100%;"></div>
  </div>
</template>

<script>
// 引入高德地图Loader
import AMapLoader from '@amap/amap-jsapi-loader';

export default {
  data() {
    return {
      selectedProvince: '', // 当前选择的省
      selectedCity: '', // 当前选择的市
      selectedDistrict: '', // 当前选择的区
      searchAddress: '', // 搜索框输入的地址
      provinces: [ // 省数据
        {value: '广东省', label: '广东省'},
        {value: '江苏省', label: '江苏省'},
        {value: '浙江省', label: '浙江省'},
        {value: '山东省', label: '山东省'},
        {value: '四川省', label: '四川省'},
        {value: '河南省', label: '河南省'},
        {value: '河北省', label: '河北省'},
        {value: '湖南省', label: '湖南省'},
        {value: '湖北省', label: '湖北省'},
        {value: '福建省', label: '福建省'},
      ],
      cities: [], // 市数据
      districts: [], // 区数据
      currentWeather: { // 当前天气信息
        temperature: '--',
        condition: '--',
        futureTemperature: '--',
        futureCondition: '--'
      },
      currentTraffic: { // 当前交通信息
        status: '--',
        congestions: []
      },
      weatherData: { // 天气数据库
        '广东省': {
          '广州市': {
            '天河区': { temperature: '28', condition: '晴', futureTemperature: '27°C ~ 29°C', futureCondition: '晴转多云' },
            '越秀区': { temperature: '27', condition: '多云', futureTemperature: '26°C ~ 28°C', futureCondition: '多云' },
            '海珠区': { temperature: '28', condition: '晴', futureTemperature: '27°C ~ 29°C', futureCondition: '晴' },
            '荔湾区': { temperature: '27', condition: '多云', futureTemperature: '26°C ~ 28°C', futureCondition: '多云转阴' },
            '白云区': { temperature: '26', condition: '阴', futureTemperature: '25°C ~ 27°C', futureCondition: '阴' }
          },
          '深圳市': {
            '福田区': { temperature: '30', condition: '晴', futureTemperature: '29°C ~ 31°C', futureCondition: '晴' },
            '南山区': { temperature: '29', condition: '晴', futureTemperature: '28°C ~ 30°C', futureCondition: '晴转多云' },
            '罗湖区': { temperature: '29', condition: '多云', futureTemperature: '28°C ~ 30°C', futureCondition: '多云' },
            '宝安区': { temperature: '28', condition: '多云', futureTemperature: '27°C ~ 29°C', futureCondition: '多云' },
            '龙岗区': { temperature: '27', condition: '阴', futureTemperature: '26°C ~ 28°C', futureCondition: '阴' }
          }
        },
        '江苏省': {
          '南京市': {
            '玄武区': { temperature: '22', condition: '小雨', futureTemperature: '21°C ~ 23°C', futureCondition: '小雨转阴' },
            '秦淮区': { temperature: '22', condition: '小雨', futureTemperature: '21°C ~ 23°C', futureCondition: '小雨' },
            '鼓楼区': { temperature: '21', condition: '阴', futureTemperature: '20°C ~ 22°C', futureCondition: '阴' },
            '建邺区': { temperature: '22', condition: '小雨', futureTemperature: '21°C ~ 23°C', futureCondition: '小雨' },
            '栖霞区': { temperature: '21', condition: '阴', futureTemperature: '20°C ~ 22°C', futureCondition: '阴转多云' }
          }
        },
        '四川省': {
          '成都市': {
            '郫都区': { temperature: '20', condition: '阴', futureTemperature: '19°C ~ 21°C', futureCondition: '多云' },
            '金牛区': { temperature: '20', condition: '阴', futureTemperature: '19°C ~ 21°C', futureCondition: '阴' },
            '锦江区': { temperature: '21', condition: '多云', futureTemperature: '20°C ~ 22°C', futureCondition: '多云' },
            '青羊区': { temperature: '20', condition: '阴', futureTemperature: '19°C ~ 21°C', futureCondition: '阴' },
            '武侯区': { temperature: '21', condition: '多云', futureTemperature: '20°C ~ 22°C', futureCondition: '多云' }
          }
        }
      },
      trafficData: { // 交通数据库
        '广东省': {
          '广州市': {
            '天河区': { status: '轻度拥堵', congestions: ['天河路', '体育东路', '珠江新城'] },
            '越秀区': { status: '中度拥堵', congestions: ['东风路', '环市路', '中山路'] },
            '海珠区': { status: '畅通', congestions: [] },
            '荔湾区': { status: '轻度拥堵', congestions: ['中山七路', '康王路'] },
            '白云区': { status: '畅通', congestions: [] }
          },
          '深圳市': {
            '福田区': { status: '严重拥堵', congestions: ['深南大道', '福华路', '彩田路'] },
            '南山区': { status: '中度拥堵', congestions: ['南海大道', '科技园路'] },
            '罗湖区': { status: '轻度拥堵', congestions: ['东门路', '人民南路'] },
            '宝安区': { status: '畅通', congestions: [] },
            '龙岗区': { status: '畅通', congestions: [] }
          }
        },
        '江苏省': {
          '南京市': {
            '玄武区': { status: '畅通', congestions: [] },
            '秦淮区': { status: '轻度拥堵', congestions: ['中山南路', '汉中路'] },
            '鼓楼区': { status: '中度拥堵', congestions: ['北京西路', '中山北路'] },
            '建邺区': { status: '畅通', congestions: [] },
            '栖霞区': { status: '畅通', congestions: [] }
          }
        },
        '四川省': {
          '成都市': {
            '郫都区': { status: '畅通', congestions: [] },
            '金牛区': { status: '轻度拥堵', congestions: ['一环路北一段', '人民北路'] },
            '锦江区': { status: '中度拥堵', congestions: ['春熙路', '红星路'] },
            '青羊区': { status: '轻度拥堵', congestions: ['人民中路', '西大街'] },
            '武侯区': { status: '中度拥堵', congestions: ['人民南路', '武侯祠大街'] }
          }
        }
      },
      map: null, // 地图实例
      geocoder: null, // 地理编码实例
      marker: null, // 标记点
      cityCoordinates: { // 城市坐标数据
        '广州市': [113.264434, 23.129162],
        '深圳市': [114.057868, 22.543099],
        '珠海市': [113.57668, 22.270715],
        '南京市': [118.796877, 32.060255],
        '苏州市': [120.585315, 31.298886],
        '无锡市': [120.31191, 31.491169],
        '杭州市': [120.15507, 30.274085],
        '宁波市': [121.550357, 29.874556],
        '温州市': [120.699366, 27.994267],
        '济南市': [117.120098, 36.6512],
        '青岛市': [120.382639, 36.067082],
        '烟台市': [121.447935, 37.463822],
        '成都市': [104.066541, 30.572269],
        '绵阳市': [104.679114, 31.46745],
        '乐山市': [103.765568, 29.552106],
        '郑州市': [113.625368, 34.746599],
        '洛阳市': [112.45404, 34.619682],
        '开封市': [114.307581, 34.797239],
        '石家庄市': [114.514859, 38.042306],
        '唐山市': [118.180193, 39.630867],
        '秦皇岛市': [119.600492, 39.935385],
        '长沙市': [112.938814, 28.228209],
        '株洲市': [113.133853, 27.827986],
        '湘潭市': [112.944049, 27.829738],
        '武汉市': [114.305392, 30.593098],
        '黄石市': [115.03852, 30.199652],
        '十堰市': [110.787916, 32.646907],
        '福州市': [119.296494, 26.074507],
        '厦门市': [118.089425, 24.479833],
        '泉州市': [118.675675, 24.874132]
      },
      districtCoordinates: { // 区坐标数据
        '天河区': [113.361597, 23.124817],
        '越秀区': [113.267601, 23.128531],
        '海珠区': [113.317388, 23.08331],
        '荔湾区': [113.243038, 23.125981],
        '白云区': [113.27324, 23.157289],
        '福田区': [114.055592, 22.520973],
        '南山区': [113.92943, 22.531221],
        '罗湖区': [114.131764, 22.548171],
        '宝安区': [113.88308, 22.55329],
        '龙岗区': [114.246899, 22.720968],
        '玄武区': [118.797861, 32.048687],
        '秦淮区': [118.786088, 32.033818],
        '鼓楼区': [118.769739, 32.066336],
        '建邺区': [118.732659, 32.004538],
        '栖霞区': [118.877091, 32.147665],
        '郫都区': [103.886124, 30.808752],
        '金牛区': [104.052236, 30.691359],
        '锦江区': [104.080986, 30.657689],
        '青羊区': [104.055731, 30.667648],
        '武侯区': [104.05167, 30.630862],
        '成华区': [104.101255, 30.660122],
        '龙泉驿区': [104.269181, 30.56065],
        '青白江区': [104.250877, 30.878681],
        '新都区': [104.158705, 30.823499],
        '温江区': [103.856646, 30.682203],
        '双流区': [103.923711, 30.573243],
        '新津区': [103.811345, 30.427866]
      }
    };
  },
  methods: {
    // 处理省的选择
    handleProvinceChange(value) {
      this.selectedCity = '';
      this.selectedDistrict = '';
      this.cities = [];
      this.districts = [];
      if (value) {
        // 根据选择的省加载对应的市数据
        switch (value) {
          case '广东省':
            this.cities = [
              {value: '广州市', label: '广州市'},
              {value: '深圳市', label: '深圳市'},
              {value: '珠海市', label: '珠海市'},
              {value: '佛山市', label: '佛山市'},
              {value: '东莞市', label: '东莞市'}
            ];
            break;
          case '江苏省':
            this.cities = [
              {value: '南京市', label: '南京市'},
              {value: '苏州市', label: '苏州市'},
              {value: '无锡市', label: '无锡市'},
              {value: '常州市', label: '常州市'},
              {value: '徐州市', label: '徐州市'}
            ];
            break;
          case '浙江省':
            this.cities = [
              {value: '杭州市', label: '杭州市'},
              {value: '宁波市', label: '宁波市'},
              {value: '温州市', label: '温州市'},
              {value: '绍兴市', label: '绍兴市'},
              {value: '嘉兴市', label: '嘉兴市'}
            ];
            break;
          case '山东省':
            this.cities = [
              {value: '济南市', label: '济南市'},
              {value: '青岛市', label: '青岛市'},
              {value: '烟台市', label: '烟台市'},
              {value: '潍坊市', label: '潍坊市'},
              {value: '临沂市', label: '临沂市'}
            ];
            break;
          case '四川省':
            this.cities = [
              {value: '成都市', label: '成都市'},
              {value: '绵阳市', label: '绵阳市'},
              {value: '乐山市', label: '乐山市'},
              {value: '德阳市', label: '德阳市'},
              {value: '宜宾市', label: '宜宾市'}
            ];
            break;
          case '河南省':
            this.cities = [
              {value: '郑州市', label: '郑州市'},
              {value: '洛阳市', label: '洛阳市'},
              {value: '开封市', label: '开封市'},
              {value: '新乡市', label: '新乡市'},
              {value: '安阳市', label: '安阳市'}
            ];
            break;
          case '河北省':
            this.cities = [
              {value: '石家庄市', label: '石家庄市'},
              {value: '唐山市', label: '唐山市'},
              {value: '秦皇岛市', label: '秦皇岛市'},
              {value: '保定市', label: '保定市'},
              {value: '邯郸市', label: '邯郸市'}
            ];
            break;
          case '湖南省':
            this.cities = [
              {value: '长沙市', label: '长沙市'},
              {value: '株洲市', label: '株洲市'},
              {value: '湘潭市', label: '湘潭市'},
              {value: '衡阳市', label: '衡阳市'},
              {value: '岳阳市', label: '岳阳市'}
            ];
            break;
          case '湖北省':
            this.cities = [
              {value: '武汉市', label: '武汉市'},
              {value: '黄石市', label: '黄石市'},
              {value: '十堰市', label: '十堰市'},
              {value: '宜昌市', label: '宜昌市'},
              {value: '襄阳市', label: '襄阳市'}
            ];
            break;
          case '福建省':
            this.cities = [
              {value: '福州市', label: '福州市'},
              {value: '厦门市', label: '厦门市'},
              {value: '泉州市', label: '泉州市'},
              {value: '漳州市', label: '漳州市'},
              {value: '莆田市', label: '莆田市'}
            ];
            break;
        }
      }
    },
    // 处理市的选择
    handleCityChange(value) {
      this.selectedDistrict = '';
      this.districts = [];
      if (value) {
        // 根据选择的市加载对应的区数据
        switch (value) {
          case '广州市':
            this.districts = [
              {value: '天河区', label: '天河区'},
              {value: '越秀区', label: '越秀区'},
              {value: '海珠区', label: '海珠区'},
              {value: '荔湾区', label: '荔湾区'},
              {value: '白云区', label: '白云区'}
            ];
            break;
          case '深圳市':
            this.districts = [
              {value: '福田区', label: '福田区'},
              {value: '南山区', label: '南山区'},
              {value: '罗湖区', label: '罗湖区'},
              {value: '宝安区', label: '宝安区'},
              {value: '龙岗区', label: '龙岗区'}
            ];
            break;
          case '南京市':
            this.districts = [
              {value: '玄武区', label: '玄武区'},
              {value: '秦淮区', label: '秦淮区'},
              {value: '鼓楼区', label: '鼓楼区'},
              {value: '建邺区', label: '建邺区'},
              {value: '栖霞区', label: '栖霞区'}
            ];
            break;
          case '成都市':
            this.districts = [
              {value: '郫都区', label: '郫都区'},
              {value: '金牛区', label: '金牛区'},
              {value: '锦江区', label: '锦江区'},
              {value: '青羊区', label: '青羊区'},
              {value: '武侯区', label: '武侯区'},
              {value: '成华区', label: '成华区'},
              {value: '龙泉驿区', label: '龙泉驿区'},
              {value: '青白江区', label: '青白江区'},
              {value: '新都区', label: '新都区'},
              {value: '温江区', label: '温江区'},
              {value: '双流区', label: '双流区'},
              {value: '新津区', label: '新津区'}
            ];
            break;
        }
      }
      // 地图定位到选择的城市
      if (this.cityCoordinates[value]) {
        this.map.setCenter(this.cityCoordinates[value]);
        this.map.setZoom(12);
        this.addMarker(this.cityCoordinates[value], value);
      }
    },
    // 处理区的选择
    handleDistrictChange(value) {
      if (this.districtCoordinates[value]) {
        this.map.setCenter(this.districtCoordinates[value]);
        this.map.setZoom(14);
        this.addMarker(this.districtCoordinates[value], value);
      }
    },
    // 搜索地址
    searchLocation() {
      if (this.searchAddress) {
        this.geocoder.getLocation(this.searchAddress, (status, result) => {
          if (status === 'complete' && result.geocodes.length) {
            const location = result.geocodes[0].location;
            const addressComponent = result.geocodes[0].addressComponent;

            // 更新下拉菜单
            this.selectedProvince = addressComponent.province;
            this.selectedCity = addressComponent.city;
            this.selectedDistrict = addressComponent.district;

            // 触发省市区数据加载
            this.handleProvinceChange(this.selectedProvince);
            this.handleCityChange(this.selectedCity);

            // 定位到搜索的位置
            this.map.setCenter(location);
            this.map.setZoom(15);
            this.addMarker(location, this.searchAddress);

            // 更新天气和交通信息
            this.updateWeatherAndTraffic();
          } else {
            this.$message.error('地址搜索失败，请重试！');
          }
        });
      }
    },
    // 显示交通拥堵详情
    showTrafficDetails() {
      if (this.currentTraffic.congestions.length > 0) {
        this.$alert(`当前拥堵道路：${this.currentTraffic.congestions.join('、')}`, '交通拥堵信息', {
          confirmButtonText: '确定'
        });
      } else {
        this.$message.info('当前区域交通畅通，无拥堵道路');
      }
    },
    // 初始化地图
    initMap() {
      AMapLoader.load({
        key: '35035f44106d8f836b969f00856c3259', // 替换为你的高德地图Key
        version: '2.0',
        plugins: ['AMap.ToolBar', 'AMap.Scale', 'AMap.HawkEye', 'AMap.MapType', 'AMap.Geocoder', 'AMap.TileLayer.Traffic']
      }).then((AMap) => {
        // 创建地图实例
        this.map = new AMap.Map('map-container', {
          viewMode: '2D', // 默认使用2D地图
          zoom: 12, // 初始化地图级别
          center: [113.264434, 23.129162], // 广州市中心
          mapStyle: 'amap://styles/normal' // 设置地图样式
        });

        // 添加控件
        this.map.addControl(new AMap.ToolBar());
        this.map.addControl(new AMap.Scale());
        this.map.addControl(new AMap.HawkEye());
        this.map.addControl(new AMap.MapType());

        // 初始化地理编码器
        this.geocoder = new AMap.Geocoder({
          city: '全国'
        });

        // 添加交通图层
        const trafficLayer = new AMap.TileLayer.Traffic({
          zIndex: 10
        });
        this.map.add(trafficLayer);

        // 添加点击地图事件
        this.map.on('click', (e) => {
          this.handleMapClick(e);
        });

      }).catch(e => {
        console.error('地图加载失败:', e);
        this.$message.error('地图加载失败，请检查网络或配置');
      });
    },
    // 添加标记点
    addMarker(position, title) {
      // 清除之前的标记
      if (this.marker) {
        this.map.remove(this.marker);
      }

      // 创建新标记
      this.marker = new AMap.Marker({
        position: position,
        title: title,
        offset: new AMap.Pixel(-13, -30)
      });

      // 将标记添加到地图
      this.map.add(this.marker);

      // 设置地图中心点
      this.map.setCenter(position);
    },
    // 地图点击事件处理
    handleMapClick(e) {
      // 获取点击位置的经纬度
      const lnglat = e.lnglat;

      // 逆地理编码获取地址信息
      this.geocoder.getAddress(lnglat, (status, result) => {
        if (status === 'complete' && result.regeocode) {
          const address = result.regeocode.formattedAddress;
          const addressComponent = result.regeocode.addressComponent;

          // 更新下拉菜单
          this.selectedProvince = addressComponent.province;
          this.selectedCity = addressComponent.city;
          this.selectedDistrict = addressComponent.district;

          // 触发省市区数据加载
          this.handleProvinceChange(this.selectedProvince);
          this.handleCityChange(this.selectedCity);

          // 添加标记
          this.addMarker([lnglat.lng, lnglat.lat], address);

          // 更新搜索框
          this.searchAddress = address;

          // 更新天气和交通信息
          this.updateWeatherAndTraffic();
        }
      });
    },
    // 更新天气和交通信息
    updateWeatherAndTraffic() {
      if (this.selectedProvince && this.selectedCity && this.selectedDistrict) {
        // 更新天气信息
        const weather = this.getWeatherData();
        this.currentWeather = weather || {
          temperature: '--',
          condition: '--',
          futureTemperature: '--',
          futureCondition: '--'
        };

        // 更新交通信息
        const traffic = this.getTrafficData();
        this.currentTraffic = traffic || {
          status: '--',
          congestions: []
        };
      }
    },
    // 获取天气数据
    getWeatherData() {
      try {
        return this.weatherData[this.selectedProvince][this.selectedCity][this.selectedDistrict];
      } catch (e) {
        return null;
      }
    },
    // 获取交通数据
    getTrafficData() {
      try {
        return this.trafficData[this.selectedProvince][this.selectedCity][this.selectedDistrict];
      } catch (e) {
        return null;
      }
    },
    // 确认选择的地区信息
    confirmLocation() {
      if (this.selectedProvince && this.selectedCity && this.selectedDistrict) {
        this.$message.success(`已确认选择：${this.selectedProvince} ${this.selectedCity} ${this.selectedDistrict}`);
        // 定位到选择的区
        if (this.districtCoordinates[this.selectedDistrict]) {
          this.map.setCenter(this.districtCoordinates[this.selectedDistrict]);
          this.map.setZoom(14);
          this.addMarker(this.districtCoordinates[this.selectedDistrict], this.selectedDistrict);
        }

        // 更新天气和交通信息
        this.updateWeatherAndTraffic();
      } else {
        this.$message.warning('请先选择完整的省市区信息');
      }
    }
  },
  mounted() {
    this.initMap(); // 初始化地图
  }
};
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
}

#map-container {
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-top: 10px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 16px;
  padding: 10px;
}

.info-item i {
  margin-right: 10px;
  font-size: 20px;
  color: #409EFF;
}

.future-info {
  margin-left: 20px;
  font-size: 14px;
  color: #666;
}

.el-card {
  margin-bottom: 10px;
  height: 100%;
}

.traffic-detail-btn {
  margin-left: 20px;
  padding: 0;
  color: #F56C6C;
}

.el-row {
  margin-bottom: 10px;
}

.el-select, .el-input {
  width: 100%;
}
</style>