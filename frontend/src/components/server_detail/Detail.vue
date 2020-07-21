<template>
  <div>
    <h1 class="key">
      {{selectKey}}
    </h1>
    <div></div>
    <div class="buttonArea">
      <v-btn
        outlined
        depressed
        small
        :disabled="isDisabled"
        @click="openSetTtlDialog"
      >TTL {{ttl}} </v-btn>
      <v-btn
        outlined
        depressed
        small
        :disabled="isDisabled"
        @click="openRenameDialog"
      >Rename</v-btn>
      <v-btn
        outlined
        depressed
        small
        :disabled="isDisabled"
        @click="openDeleteDialog"
      >Delete</v-btn>
      <v-btn
        outlined
        depressed
        small
        :disabled="isDisabled"
        @click="reloadValue"
      >Reload Value</v-btn>
    </div>
    <v-select
      item-text="name"
      item-value="value"
      label="Value Type"
      dense
      v-model="format"
      :items="formatList"
      :disabled="isDisabled"
      style="width:30%; display: inline-block;"
    ></v-select>
    <v-btn
      color="primary"
      depressed
      small
      v-show="!isModify"
      :disabled="isDisabled"
      @click="setIsModify(true)"
    >Modify</v-btn>
    <v-btn
      color="primary"
      depressed
      small
      v-show="isModify"
      @click="setValue"
    >Save</v-btn>
    <v-btn
      color="error"
      depressed
      small
      v-show="isModify"
      @click="setIsModify(false)"
    >Cancel</v-btn>
    <v-textarea
      v-model="localValue"
      ref="modify-text"
      outlined
      auto-grow
      v-show="isModify"
      name="input-7-4"
    ></v-textarea>

    <pre
      v-show="!isModify && selectKey"
      ref="valueArea"
      class="valueArea"
      :class="format">
    </pre>

    <set-ttl-dialog
      :visible="setTtlDialogVisible"
      :ttl="ttl"
      @close-dialog="closeSetTtlDialog"
      @set-ttl="setTtl">
    </set-ttl-dialog>
    <delete-ttl-dialog
      :visible="setDeleteDialogVisible"
      :select-key="selectKey"
      :value="value"
      @delete-data="deleteData"
      @close-dialog="closeDeleteDialog"
    ></delete-ttl-dialog>
    <rename-dialog
      :visible="setRenameDialogVisible"
      :select-key="selectKey"
      @rename="rename"
      @close-dialog="closeRenameDialog"
    ></rename-dialog>
  </div>
</template>

<script>
import highlight from '../../plugins/highlight'
import setTtlDialog from './dialog/setTtlDialog'
import deleteDialog from './dialog/deleteDialog'
import renameDialog from './dialog/renameDialog'
import beautify from 'js-beautify'

export default {
  props: {
    selectKey: {
      type: String
    },
    selectDb: {
      type: String
    },
    value: {
      type: String
    },
    ttl: {
      type: Number
    },
    setTtlDialogVisible: {
      type: Boolean,
      required: true
    },
    setDeleteDialogVisible: {
      type: Boolean,
      required: true
    },
    setRenameDialogVisible: {
      type: Boolean,
      required: true
    }
  },
  components: {
    'set-ttl-dialog': setTtlDialog,
    'delete-ttl-dialog': deleteDialog,
    'rename-dialog': renameDialog
  },
  data () {
    return {
      formatList: [
        {
          name: 'Plain Text',
          value: 'text',
          beautify: null
        },
        {
          name: 'JSON',
          value: 'json',
          beautify: beautify.js
        },
        {
          name: 'HTML/XML',
          value: 'html',
          beautify: beautify.html
        }
      ],
      format: 'text',
      beautify: null,
      isModify: false,
      localValue: null
    }
  },
  computed: {
    isDisabled () {
      return (this.selectKey === null) || (this.selectKey.length <= 0)
    }
  },
  watch: {
    value () {
      this.isModify = false
      this.$nextTick(() => {
        this.highlightValue()
      })
    },
    format () {
      this.$nextTick(() => {
        this.highlightValue()
      })
    }
  },
  methods: {
    setIsModify (isModify) {
      this.isModify = isModify
      if (isModify) {
        this.localValue = this.value
      }
    },
    highlightValue () {
      let formattedCode = this.value
      this.formatList.forEach((formatInfo) => {
        if (this.format === formatInfo.value) {
          this.beautify = formatInfo.beautify
        }
      })
      if (this.beautify) {
        formattedCode = this.beautify(this.value, { indent_size: 2, space_in_empty_paren: true })
      }
      this.$refs['valueArea'].innerHTML = ''
      this.$refs['valueArea'].innerText = formattedCode

      highlight.highlightBlock(this.$refs['valueArea'])
    },
    setTtl (ttl) {
      this.$emit('set-ttl', ttl)
    },
    setValue () {
      this.$emit('set-value', this.localValue)
      this.isModify = false
    },
    openSetTtlDialog () {
      this.$emit('open-set-ttl-dialog')
    },
    closeSetTtlDialog () {
      this.$emit('close-set-ttl-dialog')
    },
    deleteData () {
      this.$emit('delete-data')
    },
    openDeleteDialog () {
      this.$emit('open-delete-dialog')
    },
    closeDeleteDialog () {
      this.$emit('close-delete-dialog')
    },
    reloadValue () {
      this.$emit('reload-value')
    },
    openRenameDialog () {
      this.$emit('open-rename-dialog')
    },
    rename (newKey) {
      this.$emit('rename', newKey)
    },
    closeRenameDialog () {
      this.$emit('close-rename-dialog')
    }
  }
}
</script>

<style scoped>
.code{
  background-color: #f5f5f5;
}
.key{
  color: #00000099;
}
.valueArea{
  width:100%;
}
.text{
  background-color: #282c34;
  color: #abb2bf;
  padding: 20px;
}
.buttonArea{
  padding-bottom:20px;
}
</style>
