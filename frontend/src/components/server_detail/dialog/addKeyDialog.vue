<template>
  <v-row justify="center">
    <v-dialog v-model="visible" persistent width="80%">
      <v-card>
        <v-card-title>
          {{selectAddDb}} DB Add Key
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-form
              ref="addKeyForm"
              v-model="valid"
            >
              <v-row>
                <v-col cols="12" sm="6">
                  <v-text-field
                    v-model="key"
                    outlined
                    label="Key"
                    :rules="[rules.required]"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-text-field
                    type="number"
                    v-model="ttl"
                    outlined
                    :rules="[rules.required]"
                    label="TTL"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-textarea
                    v-model="value"
                    outlined
                    label="Value"
                    :rules="[rules.required]"
                    auto-grow
                  ></v-textarea>
                </v-col>
              </v-row>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="addKey">Submit</v-btn>
          <v-btn color="blue darken-1" text @click="closeDialog">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: 'addTtlDialog',
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    selectAddDb: {
      type: Number,
      required: true
    }
  },
  data () {
    return {
      key: null,
      value: null,
      ttl: null,
      valid: true,
      rules: {
        required: value => !!value || 'Required.'
      }
    }
  },
  watch: {
    visible () {
      this.key = null
      this.value = null
      this.ttl = null
      if (this.$refs.addKeyForm) {
        this.$refs.addKeyForm.resetValidation()
      }
    }
  },
  methods: {
    addKey () {
      if (this.$refs.addKeyForm.validate()) {
        this.$emit('add-key', {
          db: this.selectAddDb,
          key: this.key,
          value: this.value,
          ttl: this.ttl
        })
      }
    },
    closeDialog () {
      this.$emit('close-add-key-dialog')
    }
  }
}
</script>

<style scoped>

</style>
