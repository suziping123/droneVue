<!-- FlightInstrument.vue -->
<template>
  <div class="flight-instrument">
    <div class="instrument-group">
      <div class="altimeter">
        <div class="gauge">
          <div class="needle" :style="altitudeStyle"></div>
          <div class="label">高度</div>
          <div class="value">{{ formattedAltitude }} m</div>
        </div>
      </div>

      <div class="airspeed">
        <div class="gauge">
          <div class="needle" :style="speedStyle"></div>
          <div class="label">空速</div>
          <div class="value">{{ formattedSpeed }} kts</div>
        </div>
      </div>

      <div class="heading">
        <div class="compass">
          <div class="card" :style="headingStyle">
            <div class="markers">
              <div v-for="n in 36" :key="n"
                   class="marker"
                   :style="{transform: `rotate(${n*10}deg)`}">
                <span v-if="n % 3 === 0" class="degree">{{ n*10 }}</span>
              </div>
            </div>
          </div>
          <div class="value">{{ formattedHeading }}°</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    altitude: Number,
    speed: Number,
    heading: Number
  },

  computed: {
    formattedAltitude() {
      return this.altitude.toFixed(0)
    },
    formattedSpeed() {
      return this.speed.toFixed(0)
    },
    formattedHeading() {
      return this.heading.toFixed(0)
    },
    altitudeStyle() {
      const deg = this.altitude / 5000 * 270 - 135
      return { transform: `rotate(${deg}deg)` }
    },
    speedStyle() {
      const deg = this.speed / 300 * 270 - 135
      return { transform: `rotate(${deg}deg)` }
    },
    headingStyle() {
      return { transform: `rotate(${-this.heading}deg)` }
    }
  }
}
</script>

<style lang="scss" scoped>
.flight-instrument {
  $gauge-size: 180px;
  $needle-color: #ff5252;

  .instrument-group {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
  }

  .gauge {
    position: relative;
    width: $gauge-size;
    height: $gauge-size;
    background: #2c3e50;
    border-radius: 50%;

    .needle {
      position: absolute;
      width: 2px;
      height: 45%;
      background: $needle-color;
      left: 50%;
      top: 5%;
      transform-origin: 50% 90%;
      transition: transform 0.3s ease;
    }

    .label {
      position: absolute;
      top: 20%;
      width: 100%;
      text-align: center;
      color: #fff;
      font-size: 16px;
    }

    .value {
      position: absolute;
      bottom: 25%;
      width: 100%;
      text-align: center;
      color: $needle-color;
      font-size: 24px;
      font-family: monospace;
    }
  }

  .compass {
    position: relative;
    width: $gauge-size;
    height: $gauge-size;
    border-radius: 50%;
    overflow: hidden;
    background: #1a252f;

    .card {
      position: absolute;
      width: 100%;
      height: 100%;
      transition: transform 0.5s ease;

      .markers {
        position: absolute;
        width: 100%;
        height: 100%;

        .marker {
          position: absolute;
          left: 49%;
          top: 5%;
          width: 2px;
          height: 10%;
          background: #4a5a6a;

          .degree {
            position: absolute;
            left: -20px;
            top: 15px;
            color: #fff;
            font-size: 12px;
            transform: rotate(-90deg);
          }
        }
      }
    }

    .value {
      position: absolute;
      bottom: 20px;
      width: 100%;
      text-align: center;
      color: $needle-color;
      font-size: 24px;
      font-family: monospace;
    }
  }
}
</style>