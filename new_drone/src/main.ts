/*
 * @Author: suziping123 3268845120@qq.com
 * @Date: 2025-12-13 19:57:36
 * @LastEditors: suziping123 3268845120@qq.com
 * @LastEditTime: 2025-12-14 19:54:56
 * @FilePath: \droneVue\new_drone\src\main.ts
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import ElementPlus from "element-plus"
import App from './App.vue'
import router from './router'

import "element-plus/dist/index.css"
const app = createApp(App)

app.use(ElementPlus)
app.use(createPinia())
app.use(router)

app.mount('#app')
