<template>
  <div>
    <v-treeview
      :active.sync="activeKeyList"
      :items="dbList"
      :load-children="showKeyList"
      :open.sync="openList"
      open-on-click
      transition
      activatable
      color="warning"
      item-key="id"
      item-text="name"
      @update:active="loadValue"
    >
      <template v-slot:prepend="{ item }">
        <div v-if="typeof item.count !== 'undefined'"
             class="row">
          <v-icon>storage</v-icon>
        </div>
        <div v-else
             class="row">
          <v-icon>vpn_key</v-icon>
        </div>
      </template>

      <template v-slot:label="{ item }">
        {{item.name}} <span v-if="item.count != null">({{item.count}})</span>
      </template>

      <template v-slot:append="{ item, open }">
        <div v-if="typeof item.count !== 'undefined'">
          <span v-show="open">
            <v-icon @click.stop="openSearchKeyDialog(item.name)">search</v-icon>
            <v-icon @click.stop="reloadKeyList(item.name)">autorenew</v-icon>
          </span>
          <v-icon @click.stop="openAddKeyDialog(item.name)">add_circle</v-icon>
        </div>
      </template>
    </v-treeview>

    <add-key-dialog
      :visible="setAddKeyDialogVisible"
      :select-add-db="selectAddDb"
      @add-key="addKey"
      @close-add-key-dialog="closeAddKeyDialog"
    ></add-key-dialog>

    <search-key-dialog
      :visible="setSearchKeyDialogVisible"
      :select-search-db="selectSearchDb"
      @search-key="searchKey"
      @close-search-key-dialog="closeSearchKeyDialog"
    ></search-key-dialog>
  </div>
</template>

<script>
import addKeyDialog from './dialog/addKeyDialog'
import searchKeyDialog from './dialog/searchKeyDialog'

const pause = ms => new Promise(resolve => setTimeout(resolve, ms))

export default {
  components: {
    'add-key-dialog': addKeyDialog,
    'search-key-dialog': searchKeyDialog
  },
  data: () => ({
    openList: [],
    activeKeyList: [],
    selectSearchDb: null
  }),
  props: {
    dbList: {
      type: Array,
      required: true
    },
    loadKeyList: {
      type: Function,
      required: true
    },
    setAddKeyDialogVisible: {
      type: Boolean,
      required: true
    },
    setSearchKeyDialogVisible: {
      type: Boolean,
      required: true
    },
    selectAddDb: {
      type: Number,
      required: true
    }
  },
  methods: {
    isOpen (db) {
      return this.openList.includes(db)
    },
    addKey (addKeyInfo) {
      this.$emit('add-key', addKeyInfo)
    },
    openAddKeyDialog (db) {
      this.$emit('set-select-add-db', parseInt(db))
      this.$emit('open-add-key-dialog')
    },
    closeAddKeyDialog () {
      this.$emit('close-add-key-dialog')
    },
    searchKey (searchKeyInfo) {
      this.reloadKeyList(searchKeyInfo.db)
        .then(() => {
          this.activeKeyList = [searchKeyInfo.db + '/' + searchKeyInfo.key]
        })
    },
    openSearchKeyDialog (db) {
      this.selectSearchDb = db
      this.$emit('open-search-key-dialog')
    },
    closeSearchKeyDialog () {
      this.$emit('close-search-key-dialog')
    },
    parsingKey (key) {
      let keySpliceList = key.split('/')
      return {
        db: keySpliceList[0],
        key: keySpliceList[1]
      }
    },
    reloadKeyList (db) {
      this.dbList[db].children = []
      return this.loadKeyList(db)
    },
    async showKeyList (item) {
      if (item.isLoad) {
        return false
      }

      await pause(200)
      return this.loadKeyList(item.name)
    },
    loadValue (combinedKey) {
      if (combinedKey.length !== 1) {
        return false
      }
      let keyObj = this.parsingKey(combinedKey[0])

      this.$emit('load-value', {
        db: keyObj.db,
        key: keyObj.key
      })
    }
  }
}
</script>

<style>
  .v-treeview-node__label{
    font-size:10px !important;
  }
  .v-treeview-node__root{
    min-height: 16px;
  }
</style>

<style scoped>
  .row{
    cursor: pointer;
  }
  .text{

  }
  .db{
    font-weight: bold;
  }
  .key{

  }
</style>
