(ns nanomsg
  (:refer-clojure :exclude [send])
  (:import (nanomsg.pubsub PubSocket SubSocket)
           (nanomsg.reqrep ReqSocket RepSocket)
           (nanomsg.pair PairSocket)
           (nanomsg.bus BusSocket)
           (nanomsg Socket Constants)))

(def ^:private supported-sockets {:pub PubSocket :sub SubSocket
                                  :req ReqSocket :rep RepSocket
                                  :bus BusSocket :pair PairSocket})
(defn socket
  "Create a new socket."
  [^clojure.lang.Keyword socktype]
  {:pre [(contains? supported-sockets socktype)]}
  (let [cls (-> socktype supported-sockets)]
    (.newInstance cls)))

(defn bind
  "Bind socket."
  [^Socket sock, ^String dir]
  (.bind sock dir)
  sock)

(defn connect
  "Connect socket to dir."
  [^Socket sock, ^String dir]
  (.connect sock dir)
  sock)

(defn subscribe
  "Subscribe to some string pattern."
  [^SubSocket sock, ^String pattern]
  {:pre [(instance? SubSocket sock)]}
  (.subscribe sock pattern))

(defn send
  "Send string data"
  [^Socket sock, ^String data & {:keys [blocking] :or {blocking true}}]
  {:pre [(string? data)]}
  (.sendString sock data blocking))

(defn recv
  "Recv data as string"
  [^Socket sock & {:keys [blocking] :or {blocking true}}]
  (.recvString sock blocking))

(defn send-bytes
  "Send bytes data"
  [^Socket sock, data & {:keys [blocking] :or {blocking true}}]
  (.sendBytes sock data blocking))

(defn recv-bytes
  "Recv data as bytes"
  [^Socket sock & {:keys [blocking] :or {blocking true}}]
  (.recvBytes sock blocking))

(defn- resolve-symbols
  []
  (into {} (for [[k v] (Constants/getSymbols)]
             [(keyword (.toLowerCase k)) v])))

(def ^{:doc "Get all symbols"}
  symbols (memoize resolve-symbols))
