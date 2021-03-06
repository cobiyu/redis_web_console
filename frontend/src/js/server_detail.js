// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from '../components/server_detail/App'
import vuetify from '../plugins/vuetify'
import store from '../store/server_detail'

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  vuetify: vuetify,
  components: { App },
  template: '<App/>'
})
