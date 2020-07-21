//// app.vue
<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title class="headline text-uppercase">
        <span>{{decodeURIComponent(serverAlias)}}</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
        text
        href="https://github.com/vuetifyjs/vuetify/releases/latest"
        target="_blank"
      >
        <span class="mr-2">Latest Release</span>
      </v-btn>
    </v-app-bar>

    <v-content>
      <div class="lnb sc">
        <ServerTree
          :db-list="dbList"
          :load-key-list="loadKeyList"
          :set-add-key-dialog-visible="setAddKeyDialogVisible"
          :set-search-key-dialog-visible="setSearchKeyDialogVisible"
          :select-add-db="selectAddDb"
          @load-value="loadValue"
          @add-key="addKey"
          @open-search-key-dialog="openSearchKeyDialog"
          @close-search-key-dialog="closeSearchKeyDialog"
          @open-add-key-dialog="openAddKeyDialog"
          @close-add-key-dialog="closeAddKeyDialog"
          @set-select-add-db="setSelectAddDb"/>
      </div>
      <div class="content">
        <Detail
          :select-key="selectKey"
          :select-db="selectDb"
          :value="value"
          :ttl="ttl"
          :set-ttl-dialog-visible="setTtlDialogVisible"
          :set-delete-dialog-visible="setDeleteDialogVisible"
          :set-rename-dialog-visible="setRenameDialogVisible"
          @set-ttl="setTtl"
          @set-value="setValue"
          @open-set-ttl-dialog="openSetTtlDialog"
          @close-set-ttl-dialog="closeSetTtlDialog"
          @delete-data="deleteData"
          @open-delete-dialog="openDeleteDialog"
          @close-delete-dialog="closeDeleteDialog"
          @reload-value="reloadValue"
          @open-rename-dialog="openRenameDialog"
          @close-rename-dialog="closeRenameDialog"
          @rename="rename"
        />
      </div>
    </v-content>
  </v-app>
</template>

<script>
import ServerTree from './ServerTree'
import Detail from './Detail'
import { mapState } from 'vuex'

export default {
  name: 'App',
  data () {
    return {
      setTtlDialogVisible: false,
      setDeleteDialogVisible: false,
      setRenameDialogVisible: false,
      setAddKeyDialogVisible: false,
      setSearchKeyDialogVisible: false,
      serverAlias: location.pathname.split('/')[1],
      selectDb: null,
      selectKey: null,
      selectAddDb: 0
    }
  },
  computed: {
    ...mapState({
      dbList: state => state.serverList.dbList,
      value: state => state.value.value,
      ttl: state => state.value.ttl
    })
  },
  components: {
    ServerTree,
    Detail
  },
  mounted () {
    this.$store.dispatch('serverList/getDbList', this.serverAlias)
  },
  methods: {
    addKey ({db, key, value, ttl}) {
      let alias = this.serverAlias
      this.$store.dispatch('value/addKey', {alias, db, key, value, ttl})
        .then(() => {
          this.loadKeyList(db)
          this.closeAddKeyDialog()
        })
    },
    // searchKey ({db, key}) {
    //   this.selectKeyList = [key]
    //   let alias = this.serverAlias
    //   this.$store.dispatch('value/searchKey', {alias, db, key})
    //     .then(() => {
    //       this.loadValue({db, key})
    //     })
    // },
    setSelectAddDb (db) {
      this.selectAddDb = db
    },
    setValue (value) {
      let alias = this.serverAlias
      let db = this.selectDb
      let key = this.selectKey
      return this.$store.dispatch('value/modifyValue', {alias, db, key, value})
    },
    loadKeyList (db) {
      return this.$store.dispatch('serverList/getKeyList', db)
    },
    loadValue ({db, key}) {
      this.selectDb = db
      this.selectKey = key
      let alias = this.serverAlias
      return this.$store.dispatch('value/getValue', {alias, db, key})
    },
    setTtl (ttl) {
      let alias = this.serverAlias
      let db = this.selectDb
      let key = this.selectKey
      this.$store.dispatch('value/modifyTtl', {alias, db, key, ttl})
        .then(() => {
          this.closeSetTtlDialog()
        })
    },
    openSetTtlDialog () {
      this.setTtlDialogVisible = true
    },
    closeSetTtlDialog () {
      this.setTtlDialogVisible = false
    },
    deleteData () {
      let alias = this.serverAlias
      let db = this.selectDb
      let key = this.selectKey
      let ttl = 0
      this.$store.dispatch('value/deleteData', {alias, db, key, ttl})
        .then(() => {
          this.selectKey = null
          this.loadKeyList(this.selectDb)
          this.closeDeleteDialog()
        })
    },
    openDeleteDialog () {
      this.setDeleteDialogVisible = true
    },
    closeDeleteDialog () {
      this.setDeleteDialogVisible = false
    },
    rename (newKey) {
      let alias = this.serverAlias
      let db = this.selectDb
      let key = this.selectKey
      this.$store.dispatch('value/rename', {alias, db, key, newKey})
        .then(() => {
          this.selectKey = null
          this.loadKeyList(this.selectDb)
          this.closeRenameDialog()
        })
    },
    openRenameDialog () {
      this.setRenameDialogVisible = true
    },
    closeRenameDialog () {
      this.setRenameDialogVisible = false
    },
    reloadValue () {
      this.loadValue({
        db: this.selectDb,
        key: this.selectKey
      })
    },
    openAddKeyDialog () {
      this.setAddKeyDialogVisible = true
    },
    closeAddKeyDialog () {
      this.setAddKeyDialogVisible = false
    },
    openSearchKeyDialog () {
      this.setSearchKeyDialogVisible = true
    },
    closeSearchKeyDialog () {
      this.setSearchKeyDialogVisible = false
    }
  }
}
</script>

<style>
  .lnb {
    visibility: visible;
    position: absolute;
    margin: 0px;
    z-index: 2;
    left: 0px;
    right: auto;
    bottom: 0px;
    height: 100%;
    width: 400px;
  }
  .sc {
    overflow: scroll;
  }
  .content{
    visibility: visible;
    position: absolute;
    margin: 0px;
    z-index: 2;
    left: 404px;
    right: 0px;
    overflow: hidden;
    padding:10px;
  }
</style>
