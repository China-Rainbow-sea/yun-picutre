<template>
  <div id="globalSider">
    <a-layout-sider v-if="loginUserStore.loginUser.id"
                    class="sider"
                    width="200"
                    breakpoint="lg"
                    collapsed-width="0"
    >
      <a-menu
        mode="inline"
        v-model:selectedKeys="current"
        :items="menuItems"
        @click="doMenuClick"
      />
    </a-layout-sider>
  </div>
</template>
<script lang="ts" setup>
import { computed, h, ref, watchEffect } from 'vue'
import { PictureOutlined, TeamOutlined, UserOutlined } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
// import { SPACE_TYPE_ENUM } from '@/constants/space.ts'
// import { listMyTeamSpaceUsingPost } from '@/api/spaceUserController.ts'
// import { message } from 'ant-design-vue'

const loginUserStore = useLoginUserStore()



// 固定的菜单列表
// 菜单列表
const menuItems = [
  {
    key: '/',
    label: '公共图库',
    icon: () => h(PictureOutlined),
  },
  {
    key: '/my_space',
    label: '我的空间',
    icon: () => h(UserOutlined),
  },
]

const router = useRouter()

// 当前选中菜单
const current = ref<string[]>([])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, failure) => {
  current.value = [to.path]
})

// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  })
}

</script>

<style scoped>
#globalSider .ant-layout-sider {
  background: none;
}
</style>
