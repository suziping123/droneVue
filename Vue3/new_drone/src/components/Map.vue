<template>
  <div id="map" style="height: 600px; width: 100%;"></div>
</template>

<script>
import { LMap, LTileLayer, LMarker, LPolyline } from 'vue2-leaflet';
import 'leaflet/dist/leaflet.css';

export default {
  components: {
    LMap,
    LTileLayer,
    LMarker,
    LPolyline
  },
  props: {
    waypoints: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      zoom: 13,
      center: [51.505, -0.09],
      map: null
    };
  },
  mounted() {
    this.initMap();
  },
  methods: {
    initMap() {
      this.map = this.$refs.map.mapObject;
      this.map.on('click', this.handleMapClick);
    },
    handleMapClick(event) {
      const newWaypoint = {
        id: Date.now(),
        lat: event.latlng.lat,
        lng: event.latlng.lng
      };
      this.$emit('add-waypoint', newWaypoint);
    }
  },
  watch: {
    waypoints: {
      handler(newWaypoints) {
        if (this.map) {
          this.map.eachLayer(layer => {
            if (layer instanceof L.Marker || layer instanceof L.Polyline) {
              this.map.removeLayer(layer);
            }
          });

          newWaypoints.forEach((waypoint, index) => {
            L.marker([waypoint.lat, waypoint.lng], {
              draggable: true
            }).on('dragend', event => {
              const updatedWaypoint = {
                id: waypoint.id,
                lat: event.target.getLatLng().lat,
                lng: event.target.getLatLng().lng
              };
              this.$emit('update-waypoint', updatedWaypoint);
            }).addTo(this.map);

            if (index > 0) {
              const prevWaypoint = newWaypoints[index - 1];
              L.polyline([[prevWaypoint.lat, prevWaypoint.lng], [waypoint.lat, waypoint.lng]], {
                color: 'blue'
              }).addTo(this.map);
            }
          });
        }
      },
      immediate: true
    }
  }
};
</script>